package ro.sapientia2015.story;

import org.springframework.test.util.ReflectionTestUtils;

import ro.sapientia2015.story.dto.StoryDTO;
import ro.sapientia2015.story.dto.StoryGroupDTO;
import ro.sapientia2015.story.model.Story;
import ro.sapientia2015.story.model.StoryGroup;

public class StoryGroupTestUtil {

    public static final Long ID = 1L;
    public static final String DESCRIPTION = "description";
    public static final String DESCRIPTION_UPDATED = "updatedDescription";
    public static final String TITLE = "title";
    public static final String TITLE_UPDATED = "updatedTitle";

    private static final String CHARACTER = "a";

    public static StoryGroupDTO createFormObject(Long id, String description, String title) {
        StoryGroupDTO dto = new StoryGroupDTO();

        dto.setId(id);
        dto.setDescription(description);
        dto.setTitle(title); 
        return dto;
    }

    public static StoryGroup createModel(Long id, String description, String title) {
        StoryGroup model = StoryGroup.getBuilder(title)
                .description(description)
                .build();

        ReflectionTestUtils.setField(model, "id", id);

        return model;
    }

    public static String createRedirectViewPath(String path) {
        StringBuilder redirectViewPath = new StringBuilder();
        redirectViewPath.append("redirect:");
        redirectViewPath.append(path);
        return redirectViewPath.toString();
    }

    public static String createStringWithLength(int length) {
        StringBuilder builder = new StringBuilder();

        for (int index = 0; index < length; index++) {
            builder.append(CHARACTER);
        }

        return builder.toString();
    }
}
