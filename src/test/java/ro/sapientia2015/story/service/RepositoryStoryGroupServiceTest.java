package ro.sapientia2015.story.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.test.util.ReflectionTestUtils;

import ro.sapientia2015.story.StoryGroupTestUtil;
import ro.sapientia2015.story.StoryTestUtil;
import ro.sapientia2015.story.dto.StoryDTO;
import ro.sapientia2015.story.dto.StoryGroupDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Story;
import ro.sapientia2015.story.model.StoryGroup;
import ro.sapientia2015.story.repository.StoryGroupRepository;
import ro.sapientia2015.story.repository.StoryRepository;
import ro.sapientia2015.story.service.RepositoryStoryService;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.mockito.Mockito.*;

/**
 * @author Kiss Tibor
 */
public class RepositoryStoryGroupServiceTest {

    private RepositoryStoryGroupService service;

    private StoryGroupRepository repositoryMock;

    @Before
    public void setUp() {
        service = new RepositoryStoryGroupService();

        repositoryMock = mock(StoryGroupRepository.class);
        ReflectionTestUtils.setField(service, "repository", repositoryMock);
    }

    @Test
    public void add() {
        StoryGroupDTO dto = StoryGroupTestUtil.createFormObject(null, StoryGroupTestUtil.DESCRIPTION, StoryGroupTestUtil.TITLE);

        service.add(dto);

        ArgumentCaptor<StoryGroup> storyArgument = ArgumentCaptor.forClass(StoryGroup.class);
        verify(repositoryMock, times(1)).save(storyArgument.capture());
        verifyNoMoreInteractions(repositoryMock);

        StoryGroup model = storyArgument.getValue();

        assertNull(model.getId());
        assertEquals(dto.getDescription(), model.getDescription());
        assertEquals(dto.getTitle(), model.getTitle());
    }

    @Test
    public void deleteById() throws NotFoundException {
        StoryGroup model = StoryGroupTestUtil.createModel(StoryGroupTestUtil.ID, StoryGroupTestUtil.DESCRIPTION, StoryGroupTestUtil.TITLE);
        when(repositoryMock.findOne(StoryGroupTestUtil.ID)).thenReturn(model);

        StoryGroup actual = service.deleteById(StoryGroupTestUtil.ID);

        verify(repositoryMock, times(1)).findOne(StoryGroupTestUtil.ID);
        verify(repositoryMock, times(1)).delete(model);
        verifyNoMoreInteractions(repositoryMock);

        assertEquals(model, actual);
    }

    @Test(expected = NotFoundException.class)
    public void deleteByIdWhenIsNotFound() throws NotFoundException {
        when(repositoryMock.findOne(StoryGroupTestUtil.ID)).thenReturn(null);

        service.deleteById(StoryGroupTestUtil.ID);

        verify(repositoryMock, times(1)).findOne(StoryGroupTestUtil.ID);
        verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    public void findAll() {
        List<StoryGroup> models = new ArrayList<StoryGroup>();
        when(repositoryMock.findAll()).thenReturn(models);

        List<StoryGroup> actual = service.findAll();

        verify(repositoryMock, times(1)).findAll();
        verifyNoMoreInteractions(repositoryMock);

        assertEquals(models, actual);
    }

    @Test
    public void findById() throws NotFoundException {
        StoryGroup model = StoryGroupTestUtil.createModel(StoryGroupTestUtil.ID, StoryGroupTestUtil.DESCRIPTION, StoryGroupTestUtil.TITLE);
        when(repositoryMock.findOne(StoryGroupTestUtil.ID)).thenReturn(model);

        StoryGroup actual = service.findById(StoryGroupTestUtil.ID);

        verify(repositoryMock, times(1)).findOne(StoryGroupTestUtil.ID);
        verifyNoMoreInteractions(repositoryMock);

        assertEquals(model, actual);
    }

    @Test(expected = NotFoundException.class)
    public void findByIdWhenIsNotFound() throws NotFoundException {
        when(repositoryMock.findOne(StoryGroupTestUtil.ID)).thenReturn(null);

        service.findById(StoryGroupTestUtil.ID);

        verify(repositoryMock, times(1)).findOne(StoryGroupTestUtil.ID);
        verifyNoMoreInteractions(repositoryMock);
    }

    @Test
    public void update() throws NotFoundException {
        StoryGroupDTO dto = StoryGroupTestUtil.createFormObject(StoryGroupTestUtil.ID, StoryGroupTestUtil.DESCRIPTION_UPDATED, StoryGroupTestUtil.TITLE_UPDATED);
        StoryGroup model = StoryGroupTestUtil.createModel(StoryGroupTestUtil.ID, StoryGroupTestUtil.DESCRIPTION, StoryGroupTestUtil.TITLE);
        when(repositoryMock.findOne(dto.getId())).thenReturn(model);

        StoryGroup actual = service.update(dto);

        verify(repositoryMock, times(1)).findOne(dto.getId());
        verifyNoMoreInteractions(repositoryMock);

        assertEquals(dto.getId(), actual.getId());
        assertEquals(dto.getDescription(), actual.getDescription());
        assertEquals(dto.getTitle(), actual.getTitle());
    }

    @Test(expected = NotFoundException.class)
    public void updateWhenIsNotFound() throws NotFoundException {
        StoryGroupDTO dto = StoryGroupTestUtil.createFormObject(StoryGroupTestUtil.ID, StoryGroupTestUtil.DESCRIPTION_UPDATED, StoryGroupTestUtil.TITLE_UPDATED);
        when(repositoryMock.findOne(dto.getId())).thenReturn(null);

        service.update(dto);

        verify(repositoryMock, times(1)).findOne(dto.getId());
        verifyNoMoreInteractions(repositoryMock);
    }
}
