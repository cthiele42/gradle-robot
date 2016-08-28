import org.robotframework.javalib.library.AnnotationLibrary;

/**
 * Created by cthiele on 30.05.16.
 */
public class TestLibrary extends AnnotationLibrary {

    private static final String KEYWORD_PATTERN = "org/roboscratch/gradle/test/**/*.class";

    public TestLibrary() {
        addKeywordPattern(KEYWORD_PATTERN);
    }

    @Override
    public String getKeywordDocumentation(String keywordName) {
        if (keywordName.equals("__intro__"))
            return "A robot keyword library for testing continuous keyword lib development.";
        return super.getKeywordDocumentation(keywordName);
    }
}
