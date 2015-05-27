package com.myicpc.service.scoreboard.eventFeed

import com.myicpc.dto.eventFeed.visitor.EventFeedVisitor
import com.myicpc.model.contest.Contest
import com.myicpc.model.eventFeed.EventFeedControl
import com.myicpc.dto.eventFeed.parser.ContestXML
import org.mockito.InjectMocks
import org.mockito.Matchers
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.invocation.InvocationOnMock
import org.mockito.stubbing.Answer

import static org.mockito.Mockito.when

/**
 * Created by roman on 3/19/2015.
 */
class EventFeedProcessorTest extends GroovyTestCase {
    private Contest contest
    private EventFeedControl eventFeedControl

    @InjectMocks
    private EventFeedProcessor eventFeedProcessor

    @Mock
    private EventFeedVisitor eventFeedVisitor

    void setUp() {
        MockitoAnnotations.initMocks(this)
        contest = new Contest(id: 13L)
        eventFeedControl = new EventFeedControl(contest: contest)
    }

    void testParseXML_Contest() {
        when(eventFeedVisitor.visit(Matchers.<ContestXML>any(ContestXML.class), contest, eventFeedControl)).then(new Answer<Object>() {
            @Override
            Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                println invocationOnMock.getArgumentAt(0, ContestXML.class).title
                return null
            }
        })

        InputStream inputStream = readFileToStream("eventFeed/eventFeed-Contest.xml")
        Reader reader = new InputStreamReader(inputStream)
        eventFeedProcessor.parseXML(reader, contest, eventFeedControl)
    }

    private InputStream readFileToStream(String filePath) {
        return this.getClass().getClassLoader().getResourceAsStream(filePath);
    }
}
