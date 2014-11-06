var mediaToAbbreviation = {'GALLERY': 'ga', 'INSTAGRAM_IMAGE': 'im', 'VINE': 'vi', 'INSTAGRAM_VIDEO': 'iv', 'YOUTUBE_VIDEO': 'yt'};
var continents = {'EU': {'name': 'Europe', 'id': 'Europe'}, 'NA': {'name': "North America", 'id': "North America"}, 'AS': {'name': "Asia", 'id': "Asia"}, 'SA': {'name': "Latin America", 'id': "Latin America"}, 'AF': {'name': "Africa &  M. East", 'id': "Africa and the Middle East"}, 'AU': {'name': "South Pacific", 'id': "South Pacific"}};
/**
 * Gets code for gallery media type
 * @param media gallery media type
 * @returns {String} code for gallery media type
 */
function socialMediaToAbbreviation(media) {
    if (mediaToAbbreviation.hasOwnProperty(media)) {
        return mediaToAbbreviation[media];
    }
    return '';
}

/**
 * Decode region based on region code
 */
function decodeContinentCode(code) {
    if (continents.hasOwnProperty(code)) {
        return continents[code];
    }
    return {};
};