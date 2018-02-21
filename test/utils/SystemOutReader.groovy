package utils

import org.junit.After
import org.junit.Before

trait SystemOutReader {

    ByteArrayOutputStream outContent = new ByteArrayOutputStream()
    ByteArrayOutputStream errContent = new ByteArrayOutputStream()

    @Before
    void setUpStreams() {
        System.setOut(new PrintStream(outContent))
        System.setErr(new PrintStream(errContent))
    }

    @After
    void cleanUpStreams() {
        System.setOut(null)
        System.setErr(null)
    }

}