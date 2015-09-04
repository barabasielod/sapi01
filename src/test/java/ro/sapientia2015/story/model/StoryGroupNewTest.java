package ro.sapientia2015.story.model;

import org.junit.Test;

import static junit.framework.Assert.*;

public class StoryGroupNewTest {

	@Test
	public void testTitle(){
		StoryGroup storygroup = StoryGroup.getBuilder("Hello").build();
		assertNotNull(storygroup.getTitle());
	}
	
}
 