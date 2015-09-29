--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: adminnotification; Type: TABLE; Schema: public; Owner: myicpc; Tablespace: 
--

CREATE TABLE adminnotification (
    id bigint NOT NULL,
    enddate timestamp without time zone NOT NULL,
    published boolean NOT NULL,
    startdate timestamp without time zone NOT NULL,
    body text,
    imageurl character varying(255),
    title character varying(255),
    videourl character varying(255),
    contestid bigint NOT NULL
);


ALTER TABLE public.adminnotification OWNER TO myicpc;

--
-- Name: adminnotification_id_seq; Type: SEQUENCE; Schema: public; Owner: myicpc
--

CREATE SEQUENCE adminnotification_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.adminnotification_id_seq OWNER TO myicpc;

--
-- Name: attendedcontest; Type: TABLE; Schema: public; Owner: myicpc; Tablespace: 
--

CREATE TABLE attendedcontest (
    id bigint NOT NULL,
    contestparticipantrole character varying(255),
    externalid bigint,
    homepageurl character varying(255),
    name character varying(255),
    year integer,
    contestparticipantid bigint
);


ALTER TABLE public.attendedcontest OWNER TO myicpc;

--
-- Name: attendedcontest_id_seq; Type: SEQUENCE; Schema: public; Owner: myicpc
--

CREATE SEQUENCE attendedcontest_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.attendedcontest_id_seq OWNER TO myicpc;

--
-- Name: blacklisteduser; Type: TABLE; Schema: public; Owner: myicpc; Tablespace: 
--

CREATE TABLE blacklisteduser (
    id bigint NOT NULL,
    blacklistedusertype character varying(255) NOT NULL,
    username character varying(255) NOT NULL,
    contestid bigint NOT NULL
);


ALTER TABLE public.blacklisteduser OWNER TO myicpc;

--
-- Name: codeinsightactivity; Type: TABLE; Schema: public; Owner: myicpc; Tablespace: 
--

CREATE TABLE codeinsightactivity (
    id bigint NOT NULL,
    difflinecount integer NOT NULL,
    externalid bigint,
    filesize bigint NOT NULL,
    linecount integer NOT NULL,
    modifytime integer NOT NULL,
    languageid bigint,
    problemid bigint NOT NULL,
    teamid bigint NOT NULL
);


ALTER TABLE public.codeinsightactivity OWNER TO myicpc;

--
-- Name: codeinsightactivity_id_seq; Type: SEQUENCE; Schema: public; Owner: myicpc
--

CREATE SEQUENCE codeinsightactivity_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.codeinsightactivity_id_seq OWNER TO myicpc;

--
-- Name: contest; Type: TABLE; Schema: public; Owner: myicpc; Tablespace: 
--

CREATE TABLE contest (
    id bigint NOT NULL,
    code character varying(255) NOT NULL,
    externalid bigint,
    hashtag character varying(255),
    length integer NOT NULL,
    name character varying(255) NOT NULL,
    penalty double precision NOT NULL,
    shortname character varying(255) NOT NULL,
    showteamnames boolean NOT NULL,
    starttime timestamp without time zone NOT NULL,
    timedifference integer,
    contestsettingsid bigint NOT NULL,
    mapconfigurationid bigint NOT NULL,
    moduleconfigurationid bigint NOT NULL,
    questconfigurationid bigint NOT NULL,
    webservicesettingsid bigint NOT NULL
);


ALTER TABLE public.contest OWNER TO myicpc;

--
-- Name: contest_id_seq; Type: SEQUENCE; Schema: public; Owner: myicpc
--

CREATE SEQUENCE contest_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.contest_id_seq OWNER TO myicpc;

--
-- Name: contestparticipant; Type: TABLE; Schema: public; Owner: myicpc; Tablespace: 
--

CREATE TABLE contestparticipant (
    id bigint NOT NULL,
    externalid bigint,
    firstname character varying(255),
    instagramusername character varying(255),
    lastname character varying(255),
    linkedinoauthsecret character varying(255),
    linkedinoauthtoken character varying(255),
    profilepictureurl character varying(255),
    twitterusername character varying(255),
    vineusername character varying(255)
);


ALTER TABLE public.contestparticipant OWNER TO myicpc;

--
-- Name: contestparticipant_id_seq; Type: SEQUENCE; Schema: public; Owner: myicpc
--

CREATE SEQUENCE contestparticipant_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.contestparticipant_id_seq OWNER TO myicpc;

--
-- Name: contestparticipantassociation; Type: TABLE; Schema: public; Owner: myicpc; Tablespace: 
--

CREATE TABLE contestparticipantassociation (
    id bigint NOT NULL,
    contestparticipantrole character varying(255),
    contestparticipantid bigint NOT NULL,
    teaminfoid bigint
);


ALTER TABLE public.contestparticipantassociation OWNER TO myicpc;

--
-- Name: contestparticipantassociation_id_seq; Type: SEQUENCE; Schema: public; Owner: myicpc
--

CREATE SEQUENCE contestparticipantassociation_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.contestparticipantassociation_id_seq OWNER TO myicpc;

--
-- Name: contestsettings; Type: TABLE; Schema: public; Owner: myicpc; Tablespace: 
--

CREATE TABLE contestsettings (
    id bigint NOT NULL,
    jsonscoreboardurl character varying(255),
    editactivityurl character varying(255),
    email character varying(255),
    eventfeedpassword character varying(255),
    eventfeedurl character varying(255),
    eventfeedusername character varying(255),
    generatemessages boolean NOT NULL,
    livestreambackupurl character varying(255),
    livestreamurl character varying(255),
    problempdfurl character varying(255),
    scoreboardstrategytype character varying(255),
    showcountry boolean NOT NULL,
    showregion boolean NOT NULL,
    showuniversity boolean NOT NULL,
    teampicturesurl character varying(255),
    year integer
);


ALTER TABLE public.contestsettings OWNER TO myicpc;

--
-- Name: contestsettings_id_seq; Type: SEQUENCE; Schema: public; Owner: myicpc
--

CREATE SEQUENCE contestsettings_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.contestsettings_id_seq OWNER TO myicpc;

--
-- Name: databasechangelog; Type: TABLE; Schema: public; Owner: myicpc; Tablespace: 
--

CREATE TABLE databasechangelog (
    id character varying(255) NOT NULL,
    author character varying(255) NOT NULL,
    filename character varying(255) NOT NULL,
    dateexecuted timestamp without time zone NOT NULL,
    orderexecuted integer NOT NULL,
    exectype character varying(10) NOT NULL,
    md5sum character varying(35),
    description character varying(255),
    comments character varying(255),
    tag character varying(255),
    liquibase character varying(20),
    contexts character varying(255),
    labels character varying(255)
);


ALTER TABLE public.databasechangelog OWNER TO myicpc;

--
-- Name: databasechangeloglock; Type: TABLE; Schema: public; Owner: myicpc; Tablespace: 
--

CREATE TABLE databasechangeloglock (
    id integer NOT NULL,
    locked boolean NOT NULL,
    lockgranted timestamp without time zone,
    lockedby character varying(255)
);


ALTER TABLE public.databasechangeloglock OWNER TO myicpc;

--
-- Name: event; Type: TABLE; Schema: public; Owner: myicpc; Tablespace: 
--

CREATE TABLE event (
    id bigint NOT NULL,
    enddate timestamp without time zone NOT NULL,
    published boolean NOT NULL,
    startdate timestamp without time zone NOT NULL,
    code character varying(255) NOT NULL,
    description text,
    hashtag character varying(255),
    name character varying(255) NOT NULL,
    picasatag character varying(255),
    thingstobring text,
    contestid bigint NOT NULL,
    locationid bigint,
    scheduledayid bigint NOT NULL
);


ALTER TABLE public.event OWNER TO myicpc;

--
-- Name: event_id_seq; Type: SEQUENCE; Schema: public; Owner: myicpc
--

CREATE SEQUENCE event_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.event_id_seq OWNER TO myicpc;

--
-- Name: eventfeedcontrol; Type: TABLE; Schema: public; Owner: myicpc; Tablespace: 
--

CREATE TABLE eventfeedcontrol (
    id bigint NOT NULL,
    processedrunscounter integer NOT NULL,
    contestid bigint NOT NULL
);


ALTER TABLE public.eventfeedcontrol OWNER TO myicpc;

--
-- Name: eventfeedcontrol_id_seq; Type: SEQUENCE; Schema: public; Owner: myicpc
--

CREATE SEQUENCE eventfeedcontrol_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.eventfeedcontrol_id_seq OWNER TO myicpc;

--
-- Name: eventrole; Type: TABLE; Schema: public; Owner: myicpc; Tablespace: 
--

CREATE TABLE eventrole (
    id bigint NOT NULL,
    name character varying(255) NOT NULL,
    contestid bigint NOT NULL
);


ALTER TABLE public.eventrole OWNER TO myicpc;

--
-- Name: eventrole_id_seq; Type: SEQUENCE; Schema: public; Owner: myicpc
--

CREATE SEQUENCE eventrole_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.eventrole_id_seq OWNER TO myicpc;

--
-- Name: eventroleeventassociation; Type: TABLE; Schema: public; Owner: myicpc; Tablespace: 
--

CREATE TABLE eventroleeventassociation (
    eventid bigint NOT NULL,
    eventroleid bigint NOT NULL
);


ALTER TABLE public.eventroleeventassociation OWNER TO myicpc;

--
-- Name: galleryalbum; Type: TABLE; Schema: public; Owner: myicpc; Tablespace: 
--

CREATE TABLE galleryalbum (
    id bigint NOT NULL,
    name character varying(255),
    published boolean NOT NULL,
    contestid bigint NOT NULL
);


ALTER TABLE public.galleryalbum OWNER TO myicpc;

--
-- Name: galleryalbum_id_seq; Type: SEQUENCE; Schema: public; Owner: myicpc
--

CREATE SEQUENCE galleryalbum_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.galleryalbum_id_seq OWNER TO myicpc;

--
-- Name: globals; Type: TABLE; Schema: public; Owner: myicpc; Tablespace: 
--

CREATE TABLE globals (
    id bigint NOT NULL,
    name character varying(255) NOT NULL,
    text text,
    value character varying(255)
);


ALTER TABLE public.globals OWNER TO myicpc;

--
-- Name: globals_id_seq; Type: SEQUENCE; Schema: public; Owner: myicpc
--

CREATE SEQUENCE globals_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.globals_id_seq OWNER TO myicpc;

--
-- Name: judgement; Type: TABLE; Schema: public; Owner: myicpc; Tablespace: 
--

CREATE TABLE judgement (
    id bigint NOT NULL,
    code character varying(255),
    color character varying(255),
    name character varying(255),
    contestid bigint NOT NULL
);


ALTER TABLE public.judgement OWNER TO myicpc;

--
-- Name: judgement_id_seq; Type: SEQUENCE; Schema: public; Owner: myicpc
--

CREATE SEQUENCE judgement_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.judgement_id_seq OWNER TO myicpc;

--
-- Name: kioskcontent; Type: TABLE; Schema: public; Owner: myicpc; Tablespace: 
--

CREATE TABLE kioskcontent (
    id bigint NOT NULL,
    active boolean NOT NULL,
    content text,
    name character varying(255),
    contestid bigint NOT NULL
);


ALTER TABLE public.kioskcontent OWNER TO myicpc;

--
-- Name: kioskcontent_id_seq; Type: SEQUENCE; Schema: public; Owner: myicpc
--

CREATE SEQUENCE kioskcontent_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.kioskcontent_id_seq OWNER TO myicpc;

--
-- Name: language; Type: TABLE; Schema: public; Owner: myicpc; Tablespace: 
--

CREATE TABLE language (
    id bigint NOT NULL,
    name character varying(255)
);


ALTER TABLE public.language OWNER TO myicpc;

--
-- Name: language_id_seq; Type: SEQUENCE; Schema: public; Owner: myicpc
--

CREATE SEQUENCE language_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.language_id_seq OWNER TO myicpc;

--
-- Name: lastteamproblem; Type: TABLE; Schema: public; Owner: myicpc; Tablespace: 
--

CREATE TABLE lastteamproblem (
    id bigint NOT NULL,
    contestid bigint NOT NULL,
    problemid bigint NOT NULL,
    teamid bigint NOT NULL,
    teamproblemid bigint NOT NULL
);


ALTER TABLE public.lastteamproblem OWNER TO myicpc;

--
-- Name: lastteamproblem_id_seq; Type: SEQUENCE; Schema: public; Owner: myicpc
--

CREATE SEQUENCE lastteamproblem_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.lastteamproblem_id_seq OWNER TO myicpc;

--
-- Name: location; Type: TABLE; Schema: public; Owner: myicpc; Tablespace: 
--

CREATE TABLE location (
    id bigint NOT NULL,
    code character varying(255) NOT NULL,
    googlemapurl character varying(255),
    name character varying(255) NOT NULL,
    contestid bigint NOT NULL
);


ALTER TABLE public.location OWNER TO myicpc;

--
-- Name: location_id_seq; Type: SEQUENCE; Schema: public; Owner: myicpc
--

CREATE SEQUENCE location_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.location_id_seq OWNER TO myicpc;

--
-- Name: mapconfiguration; Type: TABLE; Schema: public; Owner: myicpc; Tablespace: 
--

CREATE TABLE mapconfiguration (
    id bigint NOT NULL,
    mapconfig text,
    teamcoordinates text,
    zoomablemap boolean NOT NULL
);


ALTER TABLE public.mapconfiguration OWNER TO myicpc;

--
-- Name: mapconfiguration_id_seq; Type: SEQUENCE; Schema: public; Owner: myicpc
--

CREATE SEQUENCE mapconfiguration_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.mapconfiguration_id_seq OWNER TO myicpc;

--
-- Name: moduleconfiguration; Type: TABLE; Schema: public; Owner: myicpc; Tablespace: 
--

CREATE TABLE moduleconfiguration (
    id bigint NOT NULL,
    codeinsightmodule boolean NOT NULL,
    gallerymodule boolean NOT NULL,
    mapmodule boolean NOT NULL,
    pollmodule boolean NOT NULL,
    questmodule boolean NOT NULL,
    rssmodule boolean NOT NULL,
    schedulemodule boolean NOT NULL
);


ALTER TABLE public.moduleconfiguration OWNER TO myicpc;

--
-- Name: moduleconfiguration_id_seq; Type: SEQUENCE; Schema: public; Owner: myicpc
--

CREATE SEQUENCE moduleconfiguration_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.moduleconfiguration_id_seq OWNER TO myicpc;

--
-- Name: notification; Type: TABLE; Schema: public; Owner: myicpc; Tablespace: 
--

CREATE TABLE notification (
    id bigint NOT NULL,
    authorname character varying(255),
    authorusername character varying(255),
    body text,
    code text,
    deleted boolean NOT NULL,
    entityid bigint,
    externalid character varying(255),
    featuredeligible boolean NOT NULL,
    hashtags character varying(2048),
    imageurl character varying(1024),
    notificationtype character varying(255) NOT NULL,
    offensive boolean NOT NULL,
    parentid bigint,
    profilepictureurl character varying(1024),
    teamid bigint,
    thumbnailurl character varying(1024),
    "timestamp" timestamp without time zone,
    title character varying(1024),
    url character varying(1024),
    videourl character varying(1024),
    contestid bigint NOT NULL
);


ALTER TABLE public.notification OWNER TO myicpc;

--
-- Name: notification_id_seq; Type: SEQUENCE; Schema: public; Owner: myicpc
--

CREATE SEQUENCE notification_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.notification_id_seq OWNER TO myicpc;

--
-- Name: poll; Type: TABLE; Schema: public; Owner: myicpc; Tablespace: 
--

CREATE TABLE poll (
    id bigint NOT NULL,
    enddate timestamp without time zone NOT NULL,
    published boolean NOT NULL,
    startdate timestamp without time zone NOT NULL,
    conclusionmessage text,
    description text,
    opened boolean NOT NULL,
    pollrepresentationtype character varying(255) NOT NULL,
    question character varying(255) NOT NULL,
    contestid bigint NOT NULL,
    correctanswerid bigint
);


ALTER TABLE public.poll OWNER TO myicpc;

--
-- Name: poll_id_seq; Type: SEQUENCE; Schema: public; Owner: myicpc
--

CREATE SEQUENCE poll_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.poll_id_seq OWNER TO myicpc;

--
-- Name: polloption; Type: TABLE; Schema: public; Owner: myicpc; Tablespace: 
--

CREATE TABLE polloption (
    id bigint NOT NULL,
    name character varying(255),
    votes integer,
    pollid bigint
);


ALTER TABLE public.polloption OWNER TO myicpc;

--
-- Name: polloption_id_seq; Type: SEQUENCE; Schema: public; Owner: myicpc
--

CREATE SEQUENCE polloption_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.polloption_id_seq OWNER TO myicpc;

--
-- Name: problem; Type: TABLE; Schema: public; Owner: myicpc; Tablespace: 
--

CREATE TABLE problem (
    id bigint NOT NULL,
    code character varying(255),
    name character varying(255),
    systemid bigint,
    totaltestcases integer NOT NULL,
    contestid bigint NOT NULL
);


ALTER TABLE public.problem OWNER TO myicpc;

--
-- Name: problem_id_seq; Type: SEQUENCE; Schema: public; Owner: myicpc
--

CREATE SEQUENCE problem_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.problem_id_seq OWNER TO myicpc;

--
-- Name: questchallange_id_seq; Type: SEQUENCE; Schema: public; Owner: myicpc
--

CREATE SEQUENCE questchallange_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.questchallange_id_seq OWNER TO myicpc;

--
-- Name: questchallenge; Type: TABLE; Schema: public; Owner: myicpc; Tablespace: 
--

CREATE TABLE questchallenge (
    id bigint NOT NULL,
    enddate timestamp without time zone NOT NULL,
    published boolean NOT NULL,
    startdate timestamp without time zone NOT NULL,
    defaultpoints integer NOT NULL,
    description text,
    hashtagsuffix character varying(255) NOT NULL,
    imageurl character varying(255),
    name character varying(255),
    requiresphoto boolean NOT NULL,
    requiresvideo boolean NOT NULL,
    contestid bigint NOT NULL
);


ALTER TABLE public.questchallenge OWNER TO myicpc;

--
-- Name: questconfiguration; Type: TABLE; Schema: public; Owner: myicpc; Tablespace: 
--

CREATE TABLE questconfiguration (
    id bigint NOT NULL,
    deadline timestamp without time zone,
    hashtagprefix character varying(255),
    instructionurl character varying(255),
    pointsforvote integer NOT NULL
);


ALTER TABLE public.questconfiguration OWNER TO myicpc;

--
-- Name: questconfiguration_id_seq; Type: SEQUENCE; Schema: public; Owner: myicpc
--

CREATE SEQUENCE questconfiguration_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.questconfiguration_id_seq OWNER TO myicpc;

--
-- Name: questleaderboard; Type: TABLE; Schema: public; Owner: myicpc; Tablespace: 
--

CREATE TABLE questleaderboard (
    id bigint NOT NULL,
    name character varying(255),
    published boolean NOT NULL,
    roles character varying(255),
    urlcode character varying(255),
    contestid bigint NOT NULL
);


ALTER TABLE public.questleaderboard OWNER TO myicpc;

--
-- Name: questleaderboard_id_seq; Type: SEQUENCE; Schema: public; Owner: myicpc
--

CREATE SEQUENCE questleaderboard_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.questleaderboard_id_seq OWNER TO myicpc;

--
-- Name: questparticipant; Type: TABLE; Schema: public; Owner: myicpc; Tablespace: 
--

CREATE TABLE questparticipant (
    id bigint NOT NULL,
    points integer NOT NULL,
    pointsadjustment integer NOT NULL,
    pointsfromvoting integer NOT NULL,
    contestid bigint NOT NULL,
    contestparticipantid bigint NOT NULL
);


ALTER TABLE public.questparticipant OWNER TO myicpc;

--
-- Name: questparticipant_id_seq; Type: SEQUENCE; Schema: public; Owner: myicpc
--

CREATE SEQUENCE questparticipant_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.questparticipant_id_seq OWNER TO myicpc;

--
-- Name: questsubmission; Type: TABLE; Schema: public; Owner: myicpc; Tablespace: 
--

CREATE TABLE questsubmission (
    id bigint NOT NULL,
    questpoints integer NOT NULL,
    reasontoreject character varying(255),
    submissionstate character varying(255) NOT NULL,
    votesubmissionstate character varying(255),
    votes integer NOT NULL,
    challengeid bigint NOT NULL,
    notificationid bigint NOT NULL,
    participantid bigint NOT NULL
);


ALTER TABLE public.questsubmission OWNER TO myicpc;

--
-- Name: questsubmission_id_seq; Type: SEQUENCE; Schema: public; Owner: myicpc
--

CREATE SEQUENCE questsubmission_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.questsubmission_id_seq OWNER TO myicpc;

--
-- Name: region; Type: TABLE; Schema: public; Owner: myicpc; Tablespace: 
--

CREATE TABLE region (
    id bigint NOT NULL,
    externalid bigint,
    name character varying(255),
    shortname character varying(255)
);


ALTER TABLE public.region OWNER TO myicpc;

--
-- Name: region_id_seq; Type: SEQUENCE; Schema: public; Owner: myicpc
--

CREATE SEQUENCE region_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.region_id_seq OWNER TO myicpc;

--
-- Name: regionalresult; Type: TABLE; Schema: public; Owner: myicpc; Tablespace: 
--

CREATE TABLE regionalresult (
    id bigint NOT NULL,
    contestid bigint,
    contestname character varying(255),
    lastproblemsolved integer NOT NULL,
    problemsolved integer NOT NULL,
    rank integer NOT NULL,
    teamname character varying(255),
    totaltime integer NOT NULL,
    teaminfoid bigint
);


ALTER TABLE public.regionalresult OWNER TO myicpc;

--
-- Name: regionresult_id_seq; Type: SEQUENCE; Schema: public; Owner: myicpc
--

CREATE SEQUENCE regionresult_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.regionresult_id_seq OWNER TO myicpc;

--
-- Name: rssfeed; Type: TABLE; Schema: public; Owner: myicpc; Tablespace: 
--

CREATE TABLE rssfeed (
    id bigint NOT NULL,
    disabled boolean NOT NULL,
    lastpulleddate timestamp without time zone,
    linktosource character varying(255),
    name character varying(255),
    url character varying(255),
    contestid bigint NOT NULL
);


ALTER TABLE public.rssfeed OWNER TO myicpc;

--
-- Name: rssfeed_id_seq; Type: SEQUENCE; Schema: public; Owner: myicpc
--

CREATE SEQUENCE rssfeed_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.rssfeed_id_seq OWNER TO myicpc;

--
-- Name: rssmessage; Type: TABLE; Schema: public; Owner: myicpc; Tablespace: 
--

CREATE TABLE rssmessage (
    id bigint NOT NULL,
    authoremail character varying(255),
    name character varying(255),
    publishdate timestamp without time zone,
    text text,
    url character varying(255),
    rssfeedid bigint NOT NULL
);


ALTER TABLE public.rssmessage OWNER TO myicpc;

--
-- Name: rssmessage_id_seq; Type: SEQUENCE; Schema: public; Owner: myicpc
--

CREATE SEQUENCE rssmessage_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.rssmessage_id_seq OWNER TO myicpc;

--
-- Name: scheduleday; Type: TABLE; Schema: public; Owner: myicpc; Tablespace: 
--

CREATE TABLE scheduleday (
    id bigint NOT NULL,
    date timestamp without time zone NOT NULL,
    dayorder integer NOT NULL,
    description text,
    name character varying(255) NOT NULL,
    contestid bigint NOT NULL
);


ALTER TABLE public.scheduleday OWNER TO myicpc;

--
-- Name: scheduleday_id_seq; Type: SEQUENCE; Schema: public; Owner: myicpc
--

CREATE SEQUENCE scheduleday_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.scheduleday_id_seq OWNER TO myicpc;

--
-- Name: socialmediauser_id_seq; Type: SEQUENCE; Schema: public; Owner: myicpc
--

CREATE SEQUENCE socialmediauser_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.socialmediauser_id_seq OWNER TO myicpc;

--
-- Name: systemuser; Type: TABLE; Schema: public; Owner: myicpc; Tablespace: 
--

CREATE TABLE systemuser (
    id bigint NOT NULL,
    enabled boolean NOT NULL,
    firstname character varying(255),
    lastname character varying(255),
    password character varying(255),
    username character varying(255) NOT NULL
);


ALTER TABLE public.systemuser OWNER TO myicpc;

--
-- Name: systemuserrole; Type: TABLE; Schema: public; Owner: myicpc; Tablespace: 
--

CREATE TABLE systemuserrole (
    id bigint NOT NULL,
    authority character varying(255),
    userid bigint
);


ALTER TABLE public.systemuserrole OWNER TO myicpc;

--
-- Name: team; Type: TABLE; Schema: public; Owner: myicpc; Tablespace: 
--

CREATE TABLE team (
    id bigint NOT NULL,
    externalid bigint NOT NULL,
    name character varying(255),
    nationality character varying(255),
    problemssolved integer,
    rank integer,
    systemid bigint,
    totaltime integer,
    contestid bigint NOT NULL,
    teaminfoid bigint
);


ALTER TABLE public.team OWNER TO myicpc;

--
-- Name: team_id_seq; Type: SEQUENCE; Schema: public; Owner: myicpc
--

CREATE SEQUENCE team_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.team_id_seq OWNER TO myicpc;

--
-- Name: teaminfo; Type: TABLE; Schema: public; Owner: myicpc; Tablespace: 
--

CREATE TABLE teaminfo (
    id bigint NOT NULL,
    abbreviation character varying(255),
    externalid bigint,
    hashtag character varying(255),
    name character varying(255),
    questionnaire text,
    shortname character varying(255),
    teamcontestid bigint,
    contestid bigint NOT NULL,
    regionid bigint,
    universityid bigint
);


ALTER TABLE public.teaminfo OWNER TO myicpc;

--
-- Name: teaminfo_id_seq; Type: SEQUENCE; Schema: public; Owner: myicpc
--

CREATE SEQUENCE teaminfo_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.teaminfo_id_seq OWNER TO myicpc;

--
-- Name: teamproblem; Type: TABLE; Schema: public; Owner: myicpc; Tablespace: 
--

CREATE TABLE teamproblem (
    id bigint NOT NULL,
    attempts integer,
    firstsolved boolean NOT NULL,
    judged boolean NOT NULL,
    language character varying(255),
    newrank integer NOT NULL,
    numtestpassed integer NOT NULL,
    oldrank integer NOT NULL,
    penalty boolean NOT NULL,
    resultcode character varying(255),
    solved boolean NOT NULL,
    systemid bigint,
    "time" double precision,
    "timestamp" timestamp without time zone,
    totalnumtests integer NOT NULL,
    problemid bigint NOT NULL,
    teamid bigint NOT NULL
);


ALTER TABLE public.teamproblem OWNER TO myicpc;

--
-- Name: teamproblem_id_seq; Type: SEQUENCE; Schema: public; Owner: myicpc
--

CREATE SEQUENCE teamproblem_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.teamproblem_id_seq OWNER TO myicpc;

--
-- Name: teamrankhistory; Type: TABLE; Schema: public; Owner: myicpc; Tablespace: 
--

CREATE TABLE teamrankhistory (
    id bigint NOT NULL,
    fromrank integer,
    rank integer,
    "timestamp" timestamp without time zone,
    teamid bigint
);


ALTER TABLE public.teamrankhistory OWNER TO myicpc;

--
-- Name: teamrankhistory_id_seq; Type: SEQUENCE; Schema: public; Owner: myicpc
--

CREATE SEQUENCE teamrankhistory_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.teamrankhistory_id_seq OWNER TO myicpc;

--
-- Name: university; Type: TABLE; Schema: public; Owner: myicpc; Tablespace: 
--

CREATE TABLE university (
    id bigint NOT NULL,
    country character varying(255),
    externalid bigint,
    homepageurl character varying(255),
    latitude double precision,
    longitude double precision,
    name character varying(255),
    shortname character varying(255),
    state character varying(255),
    twitterhash character varying(255)
);


ALTER TABLE public.university OWNER TO myicpc;

--
-- Name: university_id_seq; Type: SEQUENCE; Schema: public; Owner: myicpc
--

CREATE SEQUENCE university_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.university_id_seq OWNER TO myicpc;

--
-- Name: user_id_seq; Type: SEQUENCE; Schema: public; Owner: myicpc
--

CREATE SEQUENCE user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.user_id_seq OWNER TO myicpc;

--
-- Name: usercontestaccess; Type: TABLE; Schema: public; Owner: myicpc; Tablespace: 
--

CREATE TABLE usercontestaccess (
    id bigint NOT NULL,
    contestid bigint NOT NULL,
    systemuserid bigint NOT NULL
);


ALTER TABLE public.usercontestaccess OWNER TO myicpc;

--
-- Name: usercontestaccess_id_seq; Type: SEQUENCE; Schema: public; Owner: myicpc
--

CREATE SEQUENCE usercontestaccess_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.usercontestaccess_id_seq OWNER TO myicpc;

--
-- Name: userroles_id_seq; Type: SEQUENCE; Schema: public; Owner: myicpc
--

CREATE SEQUENCE userroles_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.userroles_id_seq OWNER TO myicpc;

--
-- Name: webservicesettings; Type: TABLE; Schema: public; Owner: myicpc; Tablespace: 
--

CREATE TABLE webservicesettings (
    id bigint NOT NULL,
    instagramkey character varying(255),
    instagramsecret character varying(255),
    picasacrowdalbumid character varying(255),
    picasapassword character varying(255),
    picasaprivatealbumid character varying(255),
    picasausername character varying(255),
    showphotosusername character varying(255),
    showpicasaphotos boolean NOT NULL,
    twitteraccesstoken character varying(255),
    twitteraccesstokensecret character varying(255),
    twitterconsumerkey character varying(255),
    twitterconsumersecret character varying(255),
    vineemail character varying(255),
    vinepassword character varying(255),
    wscmtoken character varying(255),
    youtubeusername character varying(255),
    youtubepullsince timestamp without time zone
);


ALTER TABLE public.webservicesettings OWNER TO myicpc;

--
-- Name: webservicesettings_id_seq; Type: SEQUENCE; Schema: public; Owner: myicpc
--

CREATE SEQUENCE webservicesettings_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.webservicesettings_id_seq OWNER TO myicpc;

--
-- Name: adminnotification_pkey; Type: CONSTRAINT; Schema: public; Owner: myicpc; Tablespace: 
--

ALTER TABLE ONLY adminnotification
    ADD CONSTRAINT adminnotification_pkey PRIMARY KEY (id);


--
-- Name: attendedcontest_pkey; Type: CONSTRAINT; Schema: public; Owner: myicpc; Tablespace: 
--

ALTER TABLE ONLY attendedcontest
    ADD CONSTRAINT attendedcontest_pkey PRIMARY KEY (id);


--
-- Name: blacklisteduser_pkey; Type: CONSTRAINT; Schema: public; Owner: myicpc; Tablespace: 
--

ALTER TABLE ONLY blacklisteduser
    ADD CONSTRAINT blacklisteduser_pkey PRIMARY KEY (id);


--
-- Name: codeinsightactivity_pkey; Type: CONSTRAINT; Schema: public; Owner: myicpc; Tablespace: 
--

ALTER TABLE ONLY codeinsightactivity
    ADD CONSTRAINT codeinsightactivity_pkey PRIMARY KEY (id);


--
-- Name: contest_pkey; Type: CONSTRAINT; Schema: public; Owner: myicpc; Tablespace: 
--

ALTER TABLE ONLY contest
    ADD CONSTRAINT contest_pkey PRIMARY KEY (id);


--
-- Name: contestparticipant_pkey; Type: CONSTRAINT; Schema: public; Owner: myicpc; Tablespace: 
--

ALTER TABLE ONLY contestparticipant
    ADD CONSTRAINT contestparticipant_pkey PRIMARY KEY (id);


--
-- Name: contestparticipantassociation_pkey; Type: CONSTRAINT; Schema: public; Owner: myicpc; Tablespace: 
--

ALTER TABLE ONLY contestparticipantassociation
    ADD CONSTRAINT contestparticipantassociation_pkey PRIMARY KEY (id);


--
-- Name: contestsettings_pkey; Type: CONSTRAINT; Schema: public; Owner: myicpc; Tablespace: 
--

ALTER TABLE ONLY contestsettings
    ADD CONSTRAINT contestsettings_pkey PRIMARY KEY (id);


--
-- Name: event_pkey; Type: CONSTRAINT; Schema: public; Owner: myicpc; Tablespace: 
--

ALTER TABLE ONLY event
    ADD CONSTRAINT event_pkey PRIMARY KEY (id);


--
-- Name: eventfeedcontrol_pkey; Type: CONSTRAINT; Schema: public; Owner: myicpc; Tablespace: 
--

ALTER TABLE ONLY eventfeedcontrol
    ADD CONSTRAINT eventfeedcontrol_pkey PRIMARY KEY (id);


--
-- Name: eventrole_pkey; Type: CONSTRAINT; Schema: public; Owner: myicpc; Tablespace: 
--

ALTER TABLE ONLY eventrole
    ADD CONSTRAINT eventrole_pkey PRIMARY KEY (id);


--
-- Name: eventroleeventassociation_pkey; Type: CONSTRAINT; Schema: public; Owner: myicpc; Tablespace: 
--

ALTER TABLE ONLY eventroleeventassociation
    ADD CONSTRAINT eventroleeventassociation_pkey PRIMARY KEY (eventid, eventroleid);


--
-- Name: galleryalbum_pkey; Type: CONSTRAINT; Schema: public; Owner: myicpc; Tablespace: 
--

ALTER TABLE ONLY galleryalbum
    ADD CONSTRAINT galleryalbum_pkey PRIMARY KEY (id);


--
-- Name: globals_pkey; Type: CONSTRAINT; Schema: public; Owner: myicpc; Tablespace: 
--

ALTER TABLE ONLY globals
    ADD CONSTRAINT globals_pkey PRIMARY KEY (id);


--
-- Name: judgement_pkey; Type: CONSTRAINT; Schema: public; Owner: myicpc; Tablespace: 
--

ALTER TABLE ONLY judgement
    ADD CONSTRAINT judgement_pkey PRIMARY KEY (id);


--
-- Name: kioskcontent_pkey; Type: CONSTRAINT; Schema: public; Owner: myicpc; Tablespace: 
--

ALTER TABLE ONLY kioskcontent
    ADD CONSTRAINT kioskcontent_pkey PRIMARY KEY (id);


--
-- Name: language_pkey; Type: CONSTRAINT; Schema: public; Owner: myicpc; Tablespace: 
--

ALTER TABLE ONLY language
    ADD CONSTRAINT language_pkey PRIMARY KEY (id);


--
-- Name: lastteamproblem_pkey; Type: CONSTRAINT; Schema: public; Owner: myicpc; Tablespace: 
--

ALTER TABLE ONLY lastteamproblem
    ADD CONSTRAINT lastteamproblem_pkey PRIMARY KEY (id);


--
-- Name: location_pkey; Type: CONSTRAINT; Schema: public; Owner: myicpc; Tablespace: 
--

ALTER TABLE ONLY location
    ADD CONSTRAINT location_pkey PRIMARY KEY (id);


--
-- Name: mapconfiguration_pkey; Type: CONSTRAINT; Schema: public; Owner: myicpc; Tablespace: 
--

ALTER TABLE ONLY mapconfiguration
    ADD CONSTRAINT mapconfiguration_pkey PRIMARY KEY (id);


--
-- Name: moduleconfiguration_pkey; Type: CONSTRAINT; Schema: public; Owner: myicpc; Tablespace: 
--

ALTER TABLE ONLY moduleconfiguration
    ADD CONSTRAINT moduleconfiguration_pkey PRIMARY KEY (id);


--
-- Name: notification_pkey; Type: CONSTRAINT; Schema: public; Owner: myicpc; Tablespace: 
--

ALTER TABLE ONLY notification
    ADD CONSTRAINT notification_pkey PRIMARY KEY (id);


--
-- Name: pk_databasechangeloglock; Type: CONSTRAINT; Schema: public; Owner: myicpc; Tablespace: 
--

ALTER TABLE ONLY databasechangeloglock
    ADD CONSTRAINT pk_databasechangeloglock PRIMARY KEY (id);


--
-- Name: poll_pkey; Type: CONSTRAINT; Schema: public; Owner: myicpc; Tablespace: 
--

ALTER TABLE ONLY poll
    ADD CONSTRAINT poll_pkey PRIMARY KEY (id);


--
-- Name: polloption_pkey; Type: CONSTRAINT; Schema: public; Owner: myicpc; Tablespace: 
--

ALTER TABLE ONLY polloption
    ADD CONSTRAINT polloption_pkey PRIMARY KEY (id);


--
-- Name: problem_pkey; Type: CONSTRAINT; Schema: public; Owner: myicpc; Tablespace: 
--

ALTER TABLE ONLY problem
    ADD CONSTRAINT problem_pkey PRIMARY KEY (id);


--
-- Name: questchallenge_pkey; Type: CONSTRAINT; Schema: public; Owner: myicpc; Tablespace: 
--

ALTER TABLE ONLY questchallenge
    ADD CONSTRAINT questchallenge_pkey PRIMARY KEY (id);


--
-- Name: questconfiguration_pkey; Type: CONSTRAINT; Schema: public; Owner: myicpc; Tablespace: 
--

ALTER TABLE ONLY questconfiguration
    ADD CONSTRAINT questconfiguration_pkey PRIMARY KEY (id);


--
-- Name: questleaderboard_pkey; Type: CONSTRAINT; Schema: public; Owner: myicpc; Tablespace: 
--

ALTER TABLE ONLY questleaderboard
    ADD CONSTRAINT questleaderboard_pkey PRIMARY KEY (id);


--
-- Name: questparticipant_pkey; Type: CONSTRAINT; Schema: public; Owner: myicpc; Tablespace: 
--

ALTER TABLE ONLY questparticipant
    ADD CONSTRAINT questparticipant_pkey PRIMARY KEY (id);


--
-- Name: questsubmission_pkey; Type: CONSTRAINT; Schema: public; Owner: myicpc; Tablespace: 
--

ALTER TABLE ONLY questsubmission
    ADD CONSTRAINT questsubmission_pkey PRIMARY KEY (id);


--
-- Name: region_pkey; Type: CONSTRAINT; Schema: public; Owner: myicpc; Tablespace: 
--

ALTER TABLE ONLY region
    ADD CONSTRAINT region_pkey PRIMARY KEY (id);


--
-- Name: regionalresult_pkey; Type: CONSTRAINT; Schema: public; Owner: myicpc; Tablespace: 
--

ALTER TABLE ONLY regionalresult
    ADD CONSTRAINT regionalresult_pkey PRIMARY KEY (id);


--
-- Name: rssfeed_pkey; Type: CONSTRAINT; Schema: public; Owner: myicpc; Tablespace: 
--

ALTER TABLE ONLY rssfeed
    ADD CONSTRAINT rssfeed_pkey PRIMARY KEY (id);


--
-- Name: rssmessage_pkey; Type: CONSTRAINT; Schema: public; Owner: myicpc; Tablespace: 
--

ALTER TABLE ONLY rssmessage
    ADD CONSTRAINT rssmessage_pkey PRIMARY KEY (id);


--
-- Name: scheduleday_pkey; Type: CONSTRAINT; Schema: public; Owner: myicpc; Tablespace: 
--

ALTER TABLE ONLY scheduleday
    ADD CONSTRAINT scheduleday_pkey PRIMARY KEY (id);


--
-- Name: systemuser_pkey; Type: CONSTRAINT; Schema: public; Owner: myicpc; Tablespace: 
--

ALTER TABLE ONLY systemuser
    ADD CONSTRAINT systemuser_pkey PRIMARY KEY (id);


--
-- Name: systemuserrole_pkey; Type: CONSTRAINT; Schema: public; Owner: myicpc; Tablespace: 
--

ALTER TABLE ONLY systemuserrole
    ADD CONSTRAINT systemuserrole_pkey PRIMARY KEY (id);


--
-- Name: team_pkey; Type: CONSTRAINT; Schema: public; Owner: myicpc; Tablespace: 
--

ALTER TABLE ONLY team
    ADD CONSTRAINT team_pkey PRIMARY KEY (id);


--
-- Name: teaminfo_pkey; Type: CONSTRAINT; Schema: public; Owner: myicpc; Tablespace: 
--

ALTER TABLE ONLY teaminfo
    ADD CONSTRAINT teaminfo_pkey PRIMARY KEY (id);


--
-- Name: teamproblem_pkey; Type: CONSTRAINT; Schema: public; Owner: myicpc; Tablespace: 
--

ALTER TABLE ONLY teamproblem
    ADD CONSTRAINT teamproblem_pkey PRIMARY KEY (id);


--
-- Name: teamrankhistory_pkey; Type: CONSTRAINT; Schema: public; Owner: myicpc; Tablespace: 
--

ALTER TABLE ONLY teamrankhistory
    ADD CONSTRAINT teamrankhistory_pkey PRIMARY KEY (id);


--
-- Name: uk_1ol6dk8yfjnjyfugikfblb410; Type: CONSTRAINT; Schema: public; Owner: myicpc; Tablespace: 
--

ALTER TABLE ONLY contestparticipant
    ADD CONSTRAINT uk_1ol6dk8yfjnjyfugikfblb410 UNIQUE (vineusername);


--
-- Name: uk_25jduhh1cvrr976kggasi8mo5; Type: CONSTRAINT; Schema: public; Owner: myicpc; Tablespace: 
--

ALTER TABLE ONLY codeinsightactivity
    ADD CONSTRAINT uk_25jduhh1cvrr976kggasi8mo5 UNIQUE (externalid);


--
-- Name: uk_33s5lqrkcvmrtp6a0j75iq6q3; Type: CONSTRAINT; Schema: public; Owner: myicpc; Tablespace: 
--

ALTER TABLE ONLY teamproblem
    ADD CONSTRAINT uk_33s5lqrkcvmrtp6a0j75iq6q3 UNIQUE (systemid, teamid, problemid);


--
-- Name: uk_3eatyultd94rgyse01k6nf1nm; Type: CONSTRAINT; Schema: public; Owner: myicpc; Tablespace: 
--

ALTER TABLE ONLY problem
    ADD CONSTRAINT uk_3eatyultd94rgyse01k6nf1nm UNIQUE (code, contestid);


--
-- Name: uk_4dmj3mcyku6ccv1w2jt6lfg13; Type: CONSTRAINT; Schema: public; Owner: myicpc; Tablespace: 
--

ALTER TABLE ONLY contest
    ADD CONSTRAINT uk_4dmj3mcyku6ccv1w2jt6lfg13 UNIQUE (shortname);


--
-- Name: uk_4tfcbuq67vl0vkgirkoxh55lw; Type: CONSTRAINT; Schema: public; Owner: myicpc; Tablespace: 
--

ALTER TABLE ONLY teaminfo
    ADD CONSTRAINT uk_4tfcbuq67vl0vkgirkoxh55lw UNIQUE (externalid, contestid);


--
-- Name: uk_4xf6cb56l39q25chovvqn41kt; Type: CONSTRAINT; Schema: public; Owner: myicpc; Tablespace: 
--

ALTER TABLE ONLY contestparticipant
    ADD CONSTRAINT uk_4xf6cb56l39q25chovvqn41kt UNIQUE (twitterusername);


--
-- Name: uk_6bqlx2u6ueya3khb89df9la8a; Type: CONSTRAINT; Schema: public; Owner: myicpc; Tablespace: 
--

ALTER TABLE ONLY region
    ADD CONSTRAINT uk_6bqlx2u6ueya3khb89df9la8a UNIQUE (externalid);


--
-- Name: uk_7coh1o3mxa3qhnxa3t0epoada; Type: CONSTRAINT; Schema: public; Owner: myicpc; Tablespace: 
--

ALTER TABLE ONLY teaminfo
    ADD CONSTRAINT uk_7coh1o3mxa3qhnxa3t0epoada UNIQUE (externalid);


--
-- Name: uk_7o96mtudp5nvo25ap7in6fckr; Type: CONSTRAINT; Schema: public; Owner: myicpc; Tablespace: 
--

ALTER TABLE ONLY eventrole
    ADD CONSTRAINT uk_7o96mtudp5nvo25ap7in6fckr UNIQUE (name, contestid);


--
-- Name: uk_7uabp24970ktvntu4ajw95wgp; Type: CONSTRAINT; Schema: public; Owner: myicpc; Tablespace: 
--

ALTER TABLE ONLY contest
    ADD CONSTRAINT uk_7uabp24970ktvntu4ajw95wgp UNIQUE (name);


--
-- Name: uk_8bbgkuv63w9sq3td7f8jrmxon; Type: CONSTRAINT; Schema: public; Owner: myicpc; Tablespace: 
--

ALTER TABLE ONLY judgement
    ADD CONSTRAINT uk_8bbgkuv63w9sq3td7f8jrmxon UNIQUE (code, contestid);


--
-- Name: uk_93y0oq43pmu5rjtdjyy0uwrv; Type: CONSTRAINT; Schema: public; Owner: myicpc; Tablespace: 
--

ALTER TABLE ONLY lastteamproblem
    ADD CONSTRAINT uk_93y0oq43pmu5rjtdjyy0uwrv UNIQUE (teamid, problemid);


--
-- Name: uk_9waci7p5ts79esyfhwuek9xf2; Type: CONSTRAINT; Schema: public; Owner: myicpc; Tablespace: 
--

ALTER TABLE ONLY contest
    ADD CONSTRAINT uk_9waci7p5ts79esyfhwuek9xf2 UNIQUE (code);


--
-- Name: uk_caugt7nyehlfp5ef9adv8yucp; Type: CONSTRAINT; Schema: public; Owner: myicpc; Tablespace: 
--

ALTER TABLE ONLY systemuser
    ADD CONSTRAINT uk_caugt7nyehlfp5ef9adv8yucp UNIQUE (username);


--
-- Name: uk_edqfrcyhr6ahhg060qqecm70u; Type: CONSTRAINT; Schema: public; Owner: myicpc; Tablespace: 
--

ALTER TABLE ONLY team
    ADD CONSTRAINT uk_edqfrcyhr6ahhg060qqecm70u UNIQUE (systemid, contestid);


--
-- Name: uk_esrdr15pfr0i3au1kxjlyn0no; Type: CONSTRAINT; Schema: public; Owner: myicpc; Tablespace: 
--

ALTER TABLE ONLY galleryalbum
    ADD CONSTRAINT uk_esrdr15pfr0i3au1kxjlyn0no UNIQUE (name, contestid);


--
-- Name: uk_estysrwxihcn984yl672je5xg; Type: CONSTRAINT; Schema: public; Owner: myicpc; Tablespace: 
--

ALTER TABLE ONLY eventfeedcontrol
    ADD CONSTRAINT uk_estysrwxihcn984yl672je5xg UNIQUE (contestid);


--
-- Name: uk_h8vncdlyoa4j8mipwx022ir16; Type: CONSTRAINT; Schema: public; Owner: myicpc; Tablespace: 
--

ALTER TABLE ONLY university
    ADD CONSTRAINT uk_h8vncdlyoa4j8mipwx022ir16 UNIQUE (externalid);


--
-- Name: uk_hfgfl1q7ukstntmyj0qxpxcv0; Type: CONSTRAINT; Schema: public; Owner: myicpc; Tablespace: 
--

ALTER TABLE ONLY team
    ADD CONSTRAINT uk_hfgfl1q7ukstntmyj0qxpxcv0 UNIQUE (externalid);


--
-- Name: uk_hh6fvrgvbg0ck7de8t557qm7f; Type: CONSTRAINT; Schema: public; Owner: myicpc; Tablespace: 
--

ALTER TABLE ONLY lastteamproblem
    ADD CONSTRAINT uk_hh6fvrgvbg0ck7de8t557qm7f UNIQUE (teamid, problemid, teamproblemid);


--
-- Name: uk_i4p38pucdd30aec8ikr2bhs9f; Type: CONSTRAINT; Schema: public; Owner: myicpc; Tablespace: 
--

ALTER TABLE ONLY scheduleday
    ADD CONSTRAINT uk_i4p38pucdd30aec8ikr2bhs9f UNIQUE (dayorder, contestid);


--
-- Name: uk_ixsakcycsblca6kwdy7rouyu4; Type: CONSTRAINT; Schema: public; Owner: myicpc; Tablespace: 
--

ALTER TABLE ONLY rssfeed
    ADD CONSTRAINT uk_ixsakcycsblca6kwdy7rouyu4 UNIQUE (url, contestid);


--
-- Name: uk_j0igwrd077mvl6m0fgmcwkfvb; Type: CONSTRAINT; Schema: public; Owner: myicpc; Tablespace: 
--

ALTER TABLE ONLY systemuserrole
    ADD CONSTRAINT uk_j0igwrd077mvl6m0fgmcwkfvb UNIQUE (userid, authority);


--
-- Name: uk_k5a3ahp0kclow62y8pggvxwt5; Type: CONSTRAINT; Schema: public; Owner: myicpc; Tablespace: 
--

ALTER TABLE ONLY questsubmission
    ADD CONSTRAINT uk_k5a3ahp0kclow62y8pggvxwt5 UNIQUE (challengeid, participantid);


--
-- Name: uk_kc4ikvwj11l1f8ohjxdjuu2ot; Type: CONSTRAINT; Schema: public; Owner: myicpc; Tablespace: 
--

ALTER TABLE ONLY contestparticipant
    ADD CONSTRAINT uk_kc4ikvwj11l1f8ohjxdjuu2ot UNIQUE (instagramusername);


--
-- Name: uk_kjhhpi27rgegxh68n4janjay7; Type: CONSTRAINT; Schema: public; Owner: myicpc; Tablespace: 
--

ALTER TABLE ONLY usercontestaccess
    ADD CONSTRAINT uk_kjhhpi27rgegxh68n4janjay7 UNIQUE (systemuserid, contestid);


--
-- Name: uk_kwv1wa8v4feqvyqmcodbaescs; Type: CONSTRAINT; Schema: public; Owner: myicpc; Tablespace: 
--

ALTER TABLE ONLY blacklisteduser
    ADD CONSTRAINT uk_kwv1wa8v4feqvyqmcodbaescs UNIQUE (username, blacklistedusertype);


--
-- Name: uk_lah7qjnaqb2o1b0w99sx21nib; Type: CONSTRAINT; Schema: public; Owner: myicpc; Tablespace: 
--

ALTER TABLE ONLY globals
    ADD CONSTRAINT uk_lah7qjnaqb2o1b0w99sx21nib UNIQUE (name);


--
-- Name: uk_lpens92jo6x8jttnv4dauq7yq; Type: CONSTRAINT; Schema: public; Owner: myicpc; Tablespace: 
--

ALTER TABLE ONLY questparticipant
    ADD CONSTRAINT uk_lpens92jo6x8jttnv4dauq7yq UNIQUE (contestparticipantid);


--
-- Name: uk_nk4c9qcgv8el6abqd6etg77yy; Type: CONSTRAINT; Schema: public; Owner: myicpc; Tablespace: 
--

ALTER TABLE ONLY language
    ADD CONSTRAINT uk_nk4c9qcgv8el6abqd6etg77yy UNIQUE (name);


--
-- Name: uk_p24yeqbsa42al23tcqibqsqon; Type: CONSTRAINT; Schema: public; Owner: myicpc; Tablespace: 
--

ALTER TABLE ONLY questchallenge
    ADD CONSTRAINT uk_p24yeqbsa42al23tcqibqsqon UNIQUE (hashtagsuffix, contestid);


--
-- Name: uk_rk0t6f2nay1xlc80bcblv106h; Type: CONSTRAINT; Schema: public; Owner: myicpc; Tablespace: 
--

ALTER TABLE ONLY event
    ADD CONSTRAINT uk_rk0t6f2nay1xlc80bcblv106h UNIQUE (code, contestid);


--
-- Name: uk_rsgrpxb5e2mtnpa6ecw2iadms; Type: CONSTRAINT; Schema: public; Owner: myicpc; Tablespace: 
--

ALTER TABLE ONLY polloption
    ADD CONSTRAINT uk_rsgrpxb5e2mtnpa6ecw2iadms UNIQUE (name, pollid);


--
-- Name: uk_tf6u0fxsfymkifrlpj3a8vmk4; Type: CONSTRAINT; Schema: public; Owner: myicpc; Tablespace: 
--

ALTER TABLE ONLY location
    ADD CONSTRAINT uk_tf6u0fxsfymkifrlpj3a8vmk4 UNIQUE (code, contestid);


--
-- Name: university_pkey; Type: CONSTRAINT; Schema: public; Owner: myicpc; Tablespace: 
--

ALTER TABLE ONLY university
    ADD CONSTRAINT university_pkey PRIMARY KEY (id);


--
-- Name: usercontestaccess_pkey; Type: CONSTRAINT; Schema: public; Owner: myicpc; Tablespace: 
--

ALTER TABLE ONLY usercontestaccess
    ADD CONSTRAINT usercontestaccess_pkey PRIMARY KEY (id);


--
-- Name: webservicesettings_pkey; Type: CONSTRAINT; Schema: public; Owner: myicpc; Tablespace: 
--

ALTER TABLE ONLY webservicesettings
    ADD CONSTRAINT webservicesettings_pkey PRIMARY KEY (id);


--
-- Name: fk_1t0i4me1s4klbt1ymvvb967tq; Type: FK CONSTRAINT; Schema: public; Owner: myicpc
--

ALTER TABLE ONLY contest
    ADD CONSTRAINT fk_1t0i4me1s4klbt1ymvvb967tq FOREIGN KEY (mapconfigurationid) REFERENCES mapconfiguration(id);


--
-- Name: fk_218rdsh81y2qmygm9elp8ybn9; Type: FK CONSTRAINT; Schema: public; Owner: myicpc
--

ALTER TABLE ONLY questchallenge
    ADD CONSTRAINT fk_218rdsh81y2qmygm9elp8ybn9 FOREIGN KEY (contestid) REFERENCES contest(id);


--
-- Name: fk_236ai7w6mlfys6bi27shbr49n; Type: FK CONSTRAINT; Schema: public; Owner: myicpc
--

ALTER TABLE ONLY eventroleeventassociation
    ADD CONSTRAINT fk_236ai7w6mlfys6bi27shbr49n FOREIGN KEY (eventroleid) REFERENCES eventrole(id);


--
-- Name: fk_24an9nms1qo8ymqdkfycsnu52; Type: FK CONSTRAINT; Schema: public; Owner: myicpc
--

ALTER TABLE ONLY event
    ADD CONSTRAINT fk_24an9nms1qo8ymqdkfycsnu52 FOREIGN KEY (scheduledayid) REFERENCES scheduleday(id);


--
-- Name: fk_3bkquv40o299a5h7wn56e0r08; Type: FK CONSTRAINT; Schema: public; Owner: myicpc
--

ALTER TABLE ONLY location
    ADD CONSTRAINT fk_3bkquv40o299a5h7wn56e0r08 FOREIGN KEY (contestid) REFERENCES contest(id);


--
-- Name: fk_3cc2dowrxmrqq1qy1c3ct167c; Type: FK CONSTRAINT; Schema: public; Owner: myicpc
--

ALTER TABLE ONLY questsubmission
    ADD CONSTRAINT fk_3cc2dowrxmrqq1qy1c3ct167c FOREIGN KEY (notificationid) REFERENCES notification(id);


--
-- Name: fk_3jyoi5yy16qqj2o3uxfn8byd1; Type: FK CONSTRAINT; Schema: public; Owner: myicpc
--

ALTER TABLE ONLY problem
    ADD CONSTRAINT fk_3jyoi5yy16qqj2o3uxfn8byd1 FOREIGN KEY (contestid) REFERENCES contest(id);


--
-- Name: fk_409ir81nqb7hqm1y2revph8mw; Type: FK CONSTRAINT; Schema: public; Owner: myicpc
--

ALTER TABLE ONLY contestparticipantassociation
    ADD CONSTRAINT fk_409ir81nqb7hqm1y2revph8mw FOREIGN KEY (contestparticipantid) REFERENCES contestparticipant(id);


--
-- Name: fk_4ftuhovppkfbfyghibfdden38; Type: FK CONSTRAINT; Schema: public; Owner: myicpc
--

ALTER TABLE ONLY teaminfo
    ADD CONSTRAINT fk_4ftuhovppkfbfyghibfdden38 FOREIGN KEY (contestid) REFERENCES contest(id);


--
-- Name: fk_4xoyivxl6dgy7ifkdhhk70std; Type: FK CONSTRAINT; Schema: public; Owner: myicpc
--

ALTER TABLE ONLY usercontestaccess
    ADD CONSTRAINT fk_4xoyivxl6dgy7ifkdhhk70std FOREIGN KEY (contestid) REFERENCES contest(id);


--
-- Name: fk_51cah8vwahvdyficw0t0k3ry3; Type: FK CONSTRAINT; Schema: public; Owner: myicpc
--

ALTER TABLE ONLY lastteamproblem
    ADD CONSTRAINT fk_51cah8vwahvdyficw0t0k3ry3 FOREIGN KEY (contestid) REFERENCES contest(id);


--
-- Name: fk_60wpja82bwqb2vtm6lat41vun; Type: FK CONSTRAINT; Schema: public; Owner: myicpc
--

ALTER TABLE ONLY team
    ADD CONSTRAINT fk_60wpja82bwqb2vtm6lat41vun FOREIGN KEY (contestid) REFERENCES contest(id);


--
-- Name: fk_6ju8huolxkj3vswrt0yufam1u; Type: FK CONSTRAINT; Schema: public; Owner: myicpc
--

ALTER TABLE ONLY teamproblem
    ADD CONSTRAINT fk_6ju8huolxkj3vswrt0yufam1u FOREIGN KEY (problemid) REFERENCES problem(id);


--
-- Name: fk_6r9f5xbwwb34jpa0i69n6j7bp; Type: FK CONSTRAINT; Schema: public; Owner: myicpc
--

ALTER TABLE ONLY teamproblem
    ADD CONSTRAINT fk_6r9f5xbwwb34jpa0i69n6j7bp FOREIGN KEY (teamid) REFERENCES team(id);


--
-- Name: fk_6tt8jq9xu0lo0t4prdi6601qe; Type: FK CONSTRAINT; Schema: public; Owner: myicpc
--

ALTER TABLE ONLY contestparticipantassociation
    ADD CONSTRAINT fk_6tt8jq9xu0lo0t4prdi6601qe FOREIGN KEY (teaminfoid) REFERENCES teaminfo(id);


--
-- Name: fk_7gjiajo2g0umxyxo8vjvb79po; Type: FK CONSTRAINT; Schema: public; Owner: myicpc
--

ALTER TABLE ONLY judgement
    ADD CONSTRAINT fk_7gjiajo2g0umxyxo8vjvb79po FOREIGN KEY (contestid) REFERENCES contest(id);


--
-- Name: fk_7myspdvxe7e1ulcjqftctc89w; Type: FK CONSTRAINT; Schema: public; Owner: myicpc
--

ALTER TABLE ONLY teaminfo
    ADD CONSTRAINT fk_7myspdvxe7e1ulcjqftctc89w FOREIGN KEY (regionid) REFERENCES region(id);


--
-- Name: fk_8yompca6ibhq8nn5c5he85bav; Type: FK CONSTRAINT; Schema: public; Owner: myicpc
--

ALTER TABLE ONLY usercontestaccess
    ADD CONSTRAINT fk_8yompca6ibhq8nn5c5he85bav FOREIGN KEY (systemuserid) REFERENCES systemuser(id);


--
-- Name: fk_94rchsh0slokc5cl2g7hbcw1v; Type: FK CONSTRAINT; Schema: public; Owner: myicpc
--

ALTER TABLE ONLY codeinsightactivity
    ADD CONSTRAINT fk_94rchsh0slokc5cl2g7hbcw1v FOREIGN KEY (problemid) REFERENCES problem(id);


--
-- Name: fk_9ibchmkmivm65ve0tdml4b1aa; Type: FK CONSTRAINT; Schema: public; Owner: myicpc
--

ALTER TABLE ONLY contest
    ADD CONSTRAINT fk_9ibchmkmivm65ve0tdml4b1aa FOREIGN KEY (webservicesettingsid) REFERENCES webservicesettings(id);


--
-- Name: fk_9whhhbdgpqgfsugnbgub6vqh1; Type: FK CONSTRAINT; Schema: public; Owner: myicpc
--

ALTER TABLE ONLY codeinsightactivity
    ADD CONSTRAINT fk_9whhhbdgpqgfsugnbgub6vqh1 FOREIGN KEY (teamid) REFERENCES team(id);


--
-- Name: fk_a8oje0qtujvjk6bw4djftvvup; Type: FK CONSTRAINT; Schema: public; Owner: myicpc
--

ALTER TABLE ONLY eventrole
    ADD CONSTRAINT fk_a8oje0qtujvjk6bw4djftvvup FOREIGN KEY (contestid) REFERENCES contest(id);


--
-- Name: fk_bh3c57xawl8o4vjph5qq8pemg; Type: FK CONSTRAINT; Schema: public; Owner: myicpc
--

ALTER TABLE ONLY lastteamproblem
    ADD CONSTRAINT fk_bh3c57xawl8o4vjph5qq8pemg FOREIGN KEY (teamproblemid) REFERENCES teamproblem(id);


--
-- Name: fk_d84u4xs4gt4knjqhc45n71us4; Type: FK CONSTRAINT; Schema: public; Owner: myicpc
--

ALTER TABLE ONLY adminnotification
    ADD CONSTRAINT fk_d84u4xs4gt4knjqhc45n71us4 FOREIGN KEY (contestid) REFERENCES contest(id);


--
-- Name: fk_db0wjm5g116nbmjwm9goaxgiq; Type: FK CONSTRAINT; Schema: public; Owner: myicpc
--

ALTER TABLE ONLY teaminfo
    ADD CONSTRAINT fk_db0wjm5g116nbmjwm9goaxgiq FOREIGN KEY (universityid) REFERENCES university(id);


--
-- Name: fk_elcjb42q36hstnv8nm27vcpdr; Type: FK CONSTRAINT; Schema: public; Owner: myicpc
--

ALTER TABLE ONLY contest
    ADD CONSTRAINT fk_elcjb42q36hstnv8nm27vcpdr FOREIGN KEY (questconfigurationid) REFERENCES questconfiguration(id);


--
-- Name: fk_er8krg365ethkupx7lmh4rn0l; Type: FK CONSTRAINT; Schema: public; Owner: myicpc
--

ALTER TABLE ONLY contest
    ADD CONSTRAINT fk_er8krg365ethkupx7lmh4rn0l FOREIGN KEY (moduleconfigurationid) REFERENCES moduleconfiguration(id);


--
-- Name: fk_estysrwxihcn984yl672je5xg; Type: FK CONSTRAINT; Schema: public; Owner: myicpc
--

ALTER TABLE ONLY eventfeedcontrol
    ADD CONSTRAINT fk_estysrwxihcn984yl672je5xg FOREIGN KEY (contestid) REFERENCES contest(id);


--
-- Name: fk_flry0gbkf9mrveo534r4ffhmb; Type: FK CONSTRAINT; Schema: public; Owner: myicpc
--

ALTER TABLE ONLY rssfeed
    ADD CONSTRAINT fk_flry0gbkf9mrveo534r4ffhmb FOREIGN KEY (contestid) REFERENCES contest(id);


--
-- Name: fk_fvqn13d1uf7bl6onq2ompktn5; Type: FK CONSTRAINT; Schema: public; Owner: myicpc
--

ALTER TABLE ONLY poll
    ADD CONSTRAINT fk_fvqn13d1uf7bl6onq2ompktn5 FOREIGN KEY (contestid) REFERENCES contest(id);


--
-- Name: fk_hdf4qqn8s87hlf74lkqm8v6uj; Type: FK CONSTRAINT; Schema: public; Owner: myicpc
--

ALTER TABLE ONLY codeinsightactivity
    ADD CONSTRAINT fk_hdf4qqn8s87hlf74lkqm8v6uj FOREIGN KEY (languageid) REFERENCES language(id);


--
-- Name: fk_hqpmilbjokuj247hphkxpx5db; Type: FK CONSTRAINT; Schema: public; Owner: myicpc
--

ALTER TABLE ONLY questparticipant
    ADD CONSTRAINT fk_hqpmilbjokuj247hphkxpx5db FOREIGN KEY (contestid) REFERENCES contest(id);


--
-- Name: fk_i3sicwxlme6ayt325u2um37h9; Type: FK CONSTRAINT; Schema: public; Owner: myicpc
--

ALTER TABLE ONLY galleryalbum
    ADD CONSTRAINT fk_i3sicwxlme6ayt325u2um37h9 FOREIGN KEY (contestid) REFERENCES contest(id);


--
-- Name: fk_i5wt6c0hb5to0ejs6c73vj71r; Type: FK CONSTRAINT; Schema: public; Owner: myicpc
--

ALTER TABLE ONLY kioskcontent
    ADD CONSTRAINT fk_i5wt6c0hb5to0ejs6c73vj71r FOREIGN KEY (contestid) REFERENCES contest(id);


--
-- Name: fk_j7m0vsjshap50j1m5hkgw94se; Type: FK CONSTRAINT; Schema: public; Owner: myicpc
--

ALTER TABLE ONLY team
    ADD CONSTRAINT fk_j7m0vsjshap50j1m5hkgw94se FOREIGN KEY (teaminfoid) REFERENCES teaminfo(externalid);


--
-- Name: fk_jklev5pdhsbqp5rmj9gev7rsp; Type: FK CONSTRAINT; Schema: public; Owner: myicpc
--

ALTER TABLE ONLY event
    ADD CONSTRAINT fk_jklev5pdhsbqp5rmj9gev7rsp FOREIGN KEY (contestid) REFERENCES contest(id);


--
-- Name: fk_jykiugvtp802apek5fdk6utyi; Type: FK CONSTRAINT; Schema: public; Owner: myicpc
--

ALTER TABLE ONLY regionalresult
    ADD CONSTRAINT fk_jykiugvtp802apek5fdk6utyi FOREIGN KEY (teaminfoid) REFERENCES teaminfo(id);


--
-- Name: fk_lajkd7xuciro54t7unbg2gqdi; Type: FK CONSTRAINT; Schema: public; Owner: myicpc
--

ALTER TABLE ONLY blacklisteduser
    ADD CONSTRAINT fk_lajkd7xuciro54t7unbg2gqdi FOREIGN KEY (contestid) REFERENCES contest(id);


--
-- Name: fk_lgyrb0sy8xmnp56hpmnjck806; Type: FK CONSTRAINT; Schema: public; Owner: myicpc
--

ALTER TABLE ONLY systemuserrole
    ADD CONSTRAINT fk_lgyrb0sy8xmnp56hpmnjck806 FOREIGN KEY (userid) REFERENCES systemuser(id);


--
-- Name: fk_lpens92jo6x8jttnv4dauq7yq; Type: FK CONSTRAINT; Schema: public; Owner: myicpc
--

ALTER TABLE ONLY questparticipant
    ADD CONSTRAINT fk_lpens92jo6x8jttnv4dauq7yq FOREIGN KEY (contestparticipantid) REFERENCES contestparticipant(id);


--
-- Name: fk_ma5xsil9cd5a3y27xj331mgb8; Type: FK CONSTRAINT; Schema: public; Owner: myicpc
--

ALTER TABLE ONLY notification
    ADD CONSTRAINT fk_ma5xsil9cd5a3y27xj331mgb8 FOREIGN KEY (contestid) REFERENCES contest(id);


--
-- Name: fk_ntybk16t73mxy5coc7pi5hbl4; Type: FK CONSTRAINT; Schema: public; Owner: myicpc
--

ALTER TABLE ONLY event
    ADD CONSTRAINT fk_ntybk16t73mxy5coc7pi5hbl4 FOREIGN KEY (locationid) REFERENCES location(id);


--
-- Name: fk_nxeteobk1gadow4wu99y81ol2; Type: FK CONSTRAINT; Schema: public; Owner: myicpc
--

ALTER TABLE ONLY contest
    ADD CONSTRAINT fk_nxeteobk1gadow4wu99y81ol2 FOREIGN KEY (contestsettingsid) REFERENCES contestsettings(id);


--
-- Name: fk_oj6hxxadxss128ysmclbcbp47; Type: FK CONSTRAINT; Schema: public; Owner: myicpc
--

ALTER TABLE ONLY lastteamproblem
    ADD CONSTRAINT fk_oj6hxxadxss128ysmclbcbp47 FOREIGN KEY (problemid) REFERENCES problem(id);


--
-- Name: fk_op7dj8b6a67tfxpxjayd3wwli; Type: FK CONSTRAINT; Schema: public; Owner: myicpc
--

ALTER TABLE ONLY rssmessage
    ADD CONSTRAINT fk_op7dj8b6a67tfxpxjayd3wwli FOREIGN KEY (rssfeedid) REFERENCES rssfeed(id);


--
-- Name: fk_opf3cgfebag2j3wq8gphxgo7; Type: FK CONSTRAINT; Schema: public; Owner: myicpc
--

ALTER TABLE ONLY teamrankhistory
    ADD CONSTRAINT fk_opf3cgfebag2j3wq8gphxgo7 FOREIGN KEY (teamid) REFERENCES team(id);


--
-- Name: fk_p0ftdpjdw9mrre2lcaggbdcrq; Type: FK CONSTRAINT; Schema: public; Owner: myicpc
--

ALTER TABLE ONLY questleaderboard
    ADD CONSTRAINT fk_p0ftdpjdw9mrre2lcaggbdcrq FOREIGN KEY (contestid) REFERENCES contest(id);


--
-- Name: fk_pjrt9mlxy0129bg72kvdmfe7g; Type: FK CONSTRAINT; Schema: public; Owner: myicpc
--

ALTER TABLE ONLY eventroleeventassociation
    ADD CONSTRAINT fk_pjrt9mlxy0129bg72kvdmfe7g FOREIGN KEY (eventid) REFERENCES event(id);


--
-- Name: fk_pm47fqekaepew21lnhps4iqui; Type: FK CONSTRAINT; Schema: public; Owner: myicpc
--

ALTER TABLE ONLY questsubmission
    ADD CONSTRAINT fk_pm47fqekaepew21lnhps4iqui FOREIGN KEY (challengeid) REFERENCES questchallenge(id);


--
-- Name: fk_sgsmdted5vk3174l9eoppeyqe; Type: FK CONSTRAINT; Schema: public; Owner: myicpc
--

ALTER TABLE ONLY lastteamproblem
    ADD CONSTRAINT fk_sgsmdted5vk3174l9eoppeyqe FOREIGN KEY (teamid) REFERENCES team(id);


--
-- Name: fk_swf3xhic29rsimotmo4m185i2; Type: FK CONSTRAINT; Schema: public; Owner: myicpc
--

ALTER TABLE ONLY scheduleday
    ADD CONSTRAINT fk_swf3xhic29rsimotmo4m185i2 FOREIGN KEY (contestid) REFERENCES contest(id);


--
-- Name: fk_tecynieemfnf5f5ljjwyd7fy6; Type: FK CONSTRAINT; Schema: public; Owner: myicpc
--

ALTER TABLE ONLY questsubmission
    ADD CONSTRAINT fk_tecynieemfnf5f5ljjwyd7fy6 FOREIGN KEY (participantid) REFERENCES questparticipant(id);


--
-- Name: fk_tjfjrvhtk3vm9wsc12nub2cbi; Type: FK CONSTRAINT; Schema: public; Owner: myicpc
--

ALTER TABLE ONLY polloption
    ADD CONSTRAINT fk_tjfjrvhtk3vm9wsc12nub2cbi FOREIGN KEY (pollid) REFERENCES poll(id);


--
-- Name: fk_tmaok0lkull677sb8lgymeuwy; Type: FK CONSTRAINT; Schema: public; Owner: myicpc
--

ALTER TABLE ONLY attendedcontest
    ADD CONSTRAINT fk_tmaok0lkull677sb8lgymeuwy FOREIGN KEY (contestparticipantid) REFERENCES contestparticipant(id);


--
-- Name: fk_toy89qwn916luk5odemwoiuuy; Type: FK CONSTRAINT; Schema: public; Owner: myicpc
--

ALTER TABLE ONLY poll
    ADD CONSTRAINT fk_toy89qwn916luk5odemwoiuuy FOREIGN KEY (correctanswerid) REFERENCES polloption(id);


--
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--

