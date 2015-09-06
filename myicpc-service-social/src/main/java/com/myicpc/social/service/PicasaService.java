package com.myicpc.social.service;

import au.com.bytecode.opencsv.CSVReader;
import com.google.gdata.client.Service.GDataRequest;
import com.google.gdata.client.Service.GDataRequest.RequestType;
import com.google.gdata.client.photos.PicasawebService;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.media.MediaSource;
import com.google.gdata.data.media.MediaStreamSource;
import com.google.gdata.data.media.mediarss.MediaThumbnail;
import com.google.gdata.data.photos.AlbumEntry;
import com.google.gdata.data.photos.AlbumFeed;
import com.google.gdata.data.photos.GphotoEntry;
import com.google.gdata.data.photos.PhotoEntry;
import com.google.gdata.data.photos.UserFeed;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ContentType;
import com.google.gdata.util.ServiceException;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.myicpc.commons.utils.FormatUtils;
import com.myicpc.enums.NotificationType;
import com.myicpc.model.contest.Contest;
import com.myicpc.model.contest.WebServiceSettings;
import com.myicpc.model.social.GalleryAlbum;
import com.myicpc.model.social.Notification;
import com.myicpc.repository.social.GalleryAlbumRepository;
import com.myicpc.repository.social.NotificationRepository;
import com.myicpc.service.exception.BusinessValidationException;
import com.myicpc.service.exception.WebServiceException;
import com.myicpc.service.notification.NotificationBuilder;
import com.myicpc.service.publish.PublishService;
import com.myicpc.social.dto.PicasaPhoto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * This class provides services for managing Picasa
 *
 * @author Roman Smetana
 */
@Service
public class PicasaService {
    private static final String MYICPC_AUTHOR = "UPLOADED_BY_MYICPC";
    private static final String CROWD_ALBUM_NAME = "CROWD_ALBUM";
    private static final String PRIVATE_ALBUM_NAME = "PRIVATE_ALBUM";

    private static final String PICASA_PHOTOS_URL = "https://picasaweb.google.com/data/feed/api/user/%s/albumid/%s";
    private static final String PICASA_PHOTO_URL = "https://picasaweb.google.com/data/entry/api/user/%s/albumid/%s/photoid/%s";
    private static final String PICASA_ALBUMS_URL = "https://picasaweb.google.com/data/feed/api/user/%s?kind=album";
    private static final String PICASA_INSERT_ALBUM_URL = "https://picasaweb.google.com/data/feed/api/user/%s";

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private GalleryAlbumRepository galleryAlbumRepository;

    @Autowired
    private PublishService publishService;

    /**
     * Gets uploaded photos to the private album
     *
     * @param contest contest
     * @return list of photos in the private album
     * @throws WebServiceException receiving photos from Picasa failed
     * @throws BusinessValidationException Picasa configuration in not complete
     */
    public List<PicasaPhoto> getPrivatePhotos(final Contest contest) throws WebServiceException, BusinessValidationException {
        WebServiceSettings webServiceSettings = contest.getWebServiceSettings();
        if (StringUtils.isAnyEmpty(webServiceSettings.getPicasaUsername(),
                webServiceSettings.getPicasaPassword(),
                webServiceSettings.getPicasaPrivateAlbumId())) {
            throw new BusinessValidationException("galleryAdmin.picasa.photos.config.missing");
        }
        try {
            List<PicasaPhoto> photos = new ArrayList<>();
            URL searchURL = new URL(String.format(PICASA_PHOTOS_URL, webServiceSettings.getPicasaUsername(), webServiceSettings.getPicasaPrivateAlbumId()) + "?kind=photo&imgmax=800&thumbsize=160");

            // call Picasa web service
            // TODO add appName
            PicasawebService picasawebService = createPicasaService(null, webServiceSettings.getPicasaUsername(), webServiceSettings.getPicasaPassword());
            GDataRequest request = picasawebService.createRequest(RequestType.QUERY, searchURL, ContentType.APPLICATION_XML);
            request.execute();

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(request.getResponseStream());

            doc.getDocumentElement().normalize();

            NodeList entries = doc.getElementsByTagName("entry");
            // process each received record
            for (int i = 0; i < entries.getLength(); i++) {
                Node nNode = entries.item(i);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    PicasaPhoto photo = new PicasaPhoto();
                    Element element = (Element) nNode;
                    photo.setPicasaId(Long.parseLong(element.getElementsByTagName("gphoto:id").item(0).getFirstChild().getNodeValue().trim()));
                    photo.setUrl(((Element) element.getElementsByTagName("content").item(0)).getAttribute("src"));
                    NodeList descriptions = element.getElementsByTagName("media:description");
                    if (descriptions.getLength() > 0 && descriptions.item(0).getFirstChild() != null) {
                        String description = descriptions.item(0).getFirstChild().getNodeValue();
                        if (!StringUtils.isEmpty(description)) {
                            photo.setTitle(description.trim());
                        }
                    }
                    NodeList thumbnails = element.getElementsByTagName("media:thumbnail");
                    for (int j = 0; j < thumbnails.getLength(); j++) {
                        Node n = thumbnails.item(j);
                        if (n.getNodeType() == Node.ELEMENT_NODE) {
                            Element e = (Element) n;
                            photo.setThumbnailUrl(e.getAttribute("url"));
                            break;
                        }
                    }

                    photos.add(photo);
                }
            }

            return photos;
        } catch (Throwable e) {
            throw new WebServiceException(e);
        }
    }

    /**
     * Creates a private username
     *
     * @param appName google application name
     * @param username picasa username
     * @param password picasa password
     * @return created album ID
     * @throws WebServiceException album creation failed
     */
    public String createPrivateAlbum(String appName, String username, String password) throws WebServiceException {
        try {
            PicasawebService picasaService = createPicasaService(appName, username, password);
            return createAlbum(picasaService, PRIVATE_ALBUM_NAME, username);
        } catch (AuthenticationException e) {
            throw new WebServiceException(e);
        }
    }

    /**
     * Creates a public username
     *
     * @param appName google application name
     * @param username picasa username
     * @param password picasa password
     * @return created album ID
     * @throws WebServiceException album creation failed
     */
    public String createCrowdAlbum(String appName, String username, String password) throws WebServiceException {
        try {
            PicasawebService picasaService = createPicasaService(appName, username, password);
            return createAlbum(picasaService, CROWD_ALBUM_NAME, username);
        } catch (AuthenticationException e) {
            throw new WebServiceException(e);
        }
    }

    private String createAlbum(final PicasawebService picasaService, final String albumName, String picasaUsername) throws WebServiceException {
        try {
            URL feedUrl = new URL(String.format(PICASA_ALBUMS_URL, "default"));
            UserFeed myUserFeed = picasaService.getFeed(feedUrl, UserFeed.class);

            for (GphotoEntry entry : myUserFeed.getEntries()) {
                if (albumName.equalsIgnoreCase(entry.getTitle().getPlainText())) {
                    return entry.getGphotoId();
                }
            }

            AlbumEntry newAlbum = new AlbumEntry();
            newAlbum.setTitle(new PlainTextConstruct(albumName));

            URL insertUrl = new URL(String.format(PICASA_INSERT_ALBUM_URL, picasaUsername));
            AlbumEntry insertedEntry = picasaService.insert(insertUrl, newAlbum);
            return insertedEntry.getGphotoId();
        } catch (ServiceException | IOException ex) {
            throw new WebServiceException(ex);
        }
    }

    /**
     * Uploads a photo to the private album
     *
     * @param caption photo caption
     * @param file uploaded photo
     * @param contest contest
     * @throws WebServiceException Picasa service error
     * @throws BusinessValidationException Picasa configuration is not complete
     */
    public void uploadPrivatePicasaEntry(String caption, MultipartFile file, Contest contest) throws WebServiceException, BusinessValidationException {
        WebServiceSettings webServiceSettings = contest.getWebServiceSettings();
        if (StringUtils.isAnyEmpty(webServiceSettings.getPicasaUsername(),
                webServiceSettings.getPicasaPassword(),
                webServiceSettings.getPicasaPrivateAlbumId())) {
            throw new BusinessValidationException("gallery.picasa.photos.config.missing");
        }
        try (InputStream fileInputStream = file.getInputStream()) {
            URL albumPostUrl = new URL(String.format(PICASA_PHOTOS_URL, webServiceSettings.getPicasaUsername(), webServiceSettings.getPicasaPrivateAlbumId()));
            PicasawebService picasawebService = createPicasaService(null, webServiceSettings.getPicasaUsername(), webServiceSettings.getPicasaPassword());
            PhotoEntry photo = new PhotoEntry();
            photo.setTitle(new PlainTextConstruct(MYICPC_AUTHOR));
            photo.setDescription(new PlainTextConstruct(caption));

            MediaSource media = new MediaStreamSource(fileInputStream, file.getContentType());
            photo.setMediaSource(media);

            PhotoEntry returnedPhoto = picasawebService.insert(albumPostUrl, photo);
        } catch (ServiceException | IOException e) {
            throw new WebServiceException(e);
        }
    }

    /**
     * Deletes a photo from private album
     *
     * @param photoId
     *            Picasa photo ID
     * @throws WebServiceException
     */
    public void deletePrivatePhoto(String photoId, final Contest contest) throws WebServiceException {
        try {
            // TODO add appName
            PicasawebService picasawebService = createPicasaService(null, contest.getWebServiceSettings().getPicasaUsername(), contest.getWebServiceSettings().getPicasaPassword());
            URL entryUrl = new URL(String.format(PICASA_PHOTO_URL, contest.getWebServiceSettings().getPicasaUsername(), contest.getWebServiceSettings().getPicasaPrivateAlbumId(), photoId));

            PhotoEntry photoEntry = picasawebService.getEntry(entryUrl, PhotoEntry.class);
            photoEntry.delete();
        } catch (Throwable ex) {
            throw new WebServiceException(ex);
        }
    }

    /**
     * Approve a photo from private Picasa album and it moves the photo from
     * private album to the public album
     *
     * @param photoId
     *            Picasa photo ID
     * @param photoTitle
     *            photo caption
     * @throws WebServiceException
     */
    @Transactional
    public void approvePrivatePhoto(String photoId, String photoTitle, final Contest contest) throws WebServiceException {
        try {
            // Get picasa photo from web service
            // TODO add appName
            PicasawebService picasawebService = createPicasaService(null, contest.getWebServiceSettings().getPicasaUsername(), contest.getWebServiceSettings().getPicasaPassword());
            URL entryUrl = new URL(String.format(PICASA_PHOTO_URL, contest.getWebServiceSettings().getPicasaUsername(), contest.getWebServiceSettings().getPicasaPrivateAlbumId(), photoId));

            PhotoEntry photoEntry = picasawebService.getEntry(entryUrl, PhotoEntry.class);

            if (photoEntry == null) {
                throw new WebServiceException("No picasa photo with ID " + photoId + " was not found.");
            }

            photoEntry.setTitle(new PlainTextConstruct(MYICPC_AUTHOR));
            photoEntry.setDescription(new PlainTextConstruct(photoTitle));
            photoEntry.setAlbumId(contest.getWebServiceSettings().getPicasaCrowdAlbumId());
            photoEntry = photoEntry.update();

            // Create MyICPC representation of Picasa photo
            NotificationBuilder builder = new NotificationBuilder();
            builder.setContest(contest);
            builder.setNotificationType(NotificationType.PICASA);
            builder.setTitle("MyICPC Gallery");
            builder.setAuthorName("MyICPC Gallery");
            builder.setBody(photoEntry.getDescription().getPlainText());
            builder.setUrl(photoEntry.getHtmlLink().getHref());
            builder.setImageUrl(photoEntry.getMediaContents().get(0).getUrl());
            builder.setThumbnailUrl(getBestPicasaThumbnail(photoEntry.getMediaThumbnails()));

            Notification notification = builder.build();
            notificationRepository.save(notification);
            publishService.broadcastNotification(notification, contest);
        } catch (Throwable ex) {
            throw new WebServiceException(ex);
        }
    }

    private PicasawebService createPicasaService(String appName, String username, String password) throws AuthenticationException {
        // TODO replace "myicpc-baylor" with appName
        PicasawebService picasaService = new PicasawebService("myicpc-baylor");
        picasaService.setUserCredentials(username, password);
        return picasaService;
    }

    /**
     * Returns URL to Picasa thumbnail of size 300x300 px
     *
     * @param thumbnails
     *            list of thumbnails
     * @return URL of thumbnail of size 300x300 px
     */
    private static String getBestPicasaThumbnail(List<MediaThumbnail> thumbnails) {
        String thumbnail = thumbnails.get(0).getUrl();
        thumbnail = thumbnail.replaceAll("/s[0-9]{2,3}/", "/s300-c/");
        return thumbnail;
    }

    /**
     * Saves changes in {@code galleryAlbum}
     *
     * If the album is published, it publishes the notification about this album
     *
     * @param galleryAlbum updated album
     */
    @Transactional
    public void saveGalleryAlbum(final GalleryAlbum galleryAlbum) {
        galleryAlbumRepository.save(galleryAlbum);
        if (galleryAlbum.isPublished()) {
            NotificationBuilder builder = new NotificationBuilder(galleryAlbum);
            builder.setTitle(galleryAlbum.getName());
            builder.setNotificationType(NotificationType.OFFICIAL_GALLERY);
            builder.setContest(galleryAlbum.getContest());
            try {
                builder.setBody(buildGalleryAlbumNotificationBody(galleryAlbum).toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            Notification notification = notificationRepository.save(builder.build());
            publishService.broadcastNotification(notification, galleryAlbum.getContest());
        }
    }

    private JsonObject buildGalleryAlbumNotificationBody(final GalleryAlbum galleryAlbum) throws IOException, ServiceException {
        JsonObject body = new JsonObject();
        String tag = galleryAlbum.getName();
        try {
            tag = UriUtils.encodeQueryParam(tag, FormatUtils.DEFAULT_ENCODING.displayName());
        } catch (UnsupportedEncodingException e) {
        }
        tag = "event$" + tag;
        body.addProperty("tag", tag);
        Calendar cal = Calendar.getInstance();
        if (galleryAlbum.getContest().getStartTime() != null) {
            cal.setTime(galleryAlbum.getContest().getStartTime());
        }
        int year = cal.get(Calendar.YEAR);
        PicasawebService picasawebService = new PicasawebService("myicpc-baylor");
        URL url = new URL("http://picasaweb.google.com/data/feed/api/user/hq.icpc/?kind=photo&" +
                "q=Album" + year + "%20%22" + tag + "%22&thumbsize=288c&start-index=1&max-results=4&access=public");

        AlbumFeed feed = picasawebService.getFeed(url, AlbumFeed.class);
        if (feed != null) {
            body.addProperty("count", feed.getTotalResults());

            if (feed.getEntries() != null) {
                JsonArray arr = new JsonArray();
                for (GphotoEntry gphotoEntry : feed.getEntries()) {
                    AlbumEntry albumEntry = new AlbumEntry(gphotoEntry.getSelf());
                    if (albumEntry.getMediaThumbnails() != null) {
                        MediaThumbnail thumbnail = albumEntry.getMediaThumbnails().get(albumEntry.getMediaThumbnails().size() - 1);
                        arr.add(new JsonPrimitive(thumbnail.getUrl()));
                    }
                }
                body.add("photos", arr);
            }
        }
        return body;
    }

    /**
     * Import albums from CVS file
     *
     * The CVS file format is a new album name per line
     *
     * @param galleriesFile CVS file with new albums
     * @param contest contest
     */
    @Transactional
    public void importGalleryAlbums(MultipartFile galleriesFile, Contest contest) {
        try (InputStream in = galleriesFile.getInputStream();
             CSVReader galleryReader = new CSVReader(new InputStreamReader(in, FormatUtils.DEFAULT_ENCODING))) {
            String[] line;
            while ((line = galleryReader.readNext()) != null) {
                GalleryAlbum galleryAlbum = galleryAlbumRepository.findByNameAndContest(line[0], contest);
                if (galleryAlbum == null) {
                    galleryAlbum = new GalleryAlbum();
                    galleryAlbum.setContest(contest);
                    galleryAlbum.setName(line[0]);
                    galleryAlbumRepository.save(galleryAlbum);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
