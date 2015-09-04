package ro.sapientia2015.story.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.context.MessageSource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.validation.support.BindingAwareModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import ro.sapientia2015.story.StoryGroupTestUtil;
import ro.sapientia2015.story.StoryTestUtil;
import ro.sapientia2015.story.config.UnitTestContext;
import ro.sapientia2015.story.controller.StoryController;
import ro.sapientia2015.story.dto.StoryDTO;
import ro.sapientia2015.story.dto.StoryGroupDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Story;
import ro.sapientia2015.story.model.StoryGroup;
import ro.sapientia2015.story.service.StoryGroupService;
import ro.sapientia2015.story.service.StoryService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**  
 * @author Kiss Tibor
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {UnitTestContext.class})
public class StoryGroupControllerTest {

    private static final String FEEDBACK_MESSAGE = "feedbackMessage";
    private static final String FIELD_DESCRIPTION = "description";
    private static final String FIELD_TITLE = "title";

    private StoryGroupController controller;

    private MessageSource messageSourceMock;

    private StoryGroupService serviceMock;

    @Resource
    private Validator validator;

    @Before
    public void setUp() {
        controller = new StoryGroupController();

        messageSourceMock = mock(MessageSource.class);
        ReflectionTestUtils.setField(controller, "messageSource", messageSourceMock);

        serviceMock = mock(StoryGroupService.class);
        ReflectionTestUtils.setField(controller, "service", serviceMock);
    }

    @Test
    public void showAddStoryGroupForm() {
        BindingAwareModelMap model = new BindingAwareModelMap();

        String view = controller.showAddForm(model);

        verifyZeroInteractions(messageSourceMock, serviceMock);
        assertEquals(StoryController.VIEW_ADD, view);

        StoryGroupDTO formObject = (StoryGroupDTO) model.asMap().get(StoryGroupController.MODEL_ATTRIBUTE);

        assertNull(formObject.getId());
        assertNull(formObject.getDescription());
        assertNull(formObject.getTitle());
    }

    @Test
    public void add() {
        StoryGroupDTO formObject = StoryGroupTestUtil.createFormObject(null, StoryGroupTestUtil.DESCRIPTION, StoryGroupTestUtil.TITLE);

        StoryGroup model = StoryGroupTestUtil.createModel(StoryGroupTestUtil.ID, StoryGroupTestUtil.DESCRIPTION, StoryGroupTestUtil.TITLE);
        when(serviceMock.add(formObject)).thenReturn(model);

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/storygroup/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        initMessageSourceForFeedbackMessage(StoryGroupController.FEEDBACK_MESSAGE_KEY_ADDED);

        String view = controller.add(formObject, result, attributes);

        verify(serviceMock, times(1)).add(formObject);
        verifyNoMoreInteractions(serviceMock);

        String expectedView = StoryGroupTestUtil.createRedirectViewPath(StoryGroupController.REQUEST_MAPPING_VIEW);
        assertEquals(expectedView, view);

        assertEquals(Long.valueOf((String) attributes.get(StoryGroupController.PARAMETER_ID)), model.getId());

        assertFeedbackMessage(attributes, StoryGroupController.FEEDBACK_MESSAGE_KEY_ADDED);
    }

    @Test
    public void addEmptyStoryGroup() {
        StoryGroupDTO formObject = StoryGroupTestUtil.createFormObject(null, "", "");

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/storygroup/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        String view = controller.add(formObject, result, attributes);

        verifyZeroInteractions(serviceMock, messageSourceMock);

        assertEquals(StoryGroupController.VIEW_ADD, view);
        assertFieldErrors(result, FIELD_TITLE);
    }

    @Test
    public void addWithTooLongDescriptionAndTitle() {
        String description = StoryGroupTestUtil.createStringWithLength(StoryGroup.MAX_LENGTH_DESCRIPTION + 1);
        String title = StoryGroupTestUtil.createStringWithLength(StoryGroup.MAX_LENGTH_TITLE + 1);

        StoryGroupDTO formObject = StoryGroupTestUtil.createFormObject(null, description, title);

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/storygroup/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        String view = controller.add(formObject, result, attributes);

        verifyZeroInteractions(serviceMock, messageSourceMock);

        assertEquals(StoryGroupController.VIEW_ADD, view);
        assertFieldErrors(result, FIELD_DESCRIPTION, FIELD_TITLE);
    }

    @Test
    public void deleteById() throws NotFoundException {
        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        StoryGroup model = StoryGroupTestUtil.createModel(StoryGroupTestUtil.ID, StoryGroupTestUtil.DESCRIPTION, StoryGroupTestUtil.TITLE);
        when(serviceMock.deleteById(StoryGroupTestUtil.ID)).thenReturn(model);

        initMessageSourceForFeedbackMessage(StoryGroupController.FEEDBACK_MESSAGE_KEY_DELETED);

        String view = controller.deleteById(StoryGroupTestUtil.ID, attributes);

        verify(serviceMock, times(1)).deleteById(StoryGroupTestUtil.ID);
        verifyNoMoreInteractions(serviceMock);

        assertFeedbackMessage(attributes, StoryGroupController.FEEDBACK_MESSAGE_KEY_DELETED);

        String expectedView = StoryGroupTestUtil.createRedirectViewPath(StoryGroupController.REQUEST_MAPPING_LIST);
        assertEquals(expectedView, view);
    }

    @Test(expected = NotFoundException.class)
    public void deleteByIdWhenIsNotFound() throws NotFoundException {
        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        when(serviceMock.deleteById(StoryGroupTestUtil.ID)).thenThrow(new NotFoundException(""));

        controller.deleteById(StoryGroupTestUtil.ID, attributes);

        verify(serviceMock, times(1)).deleteById(StoryGroupTestUtil.ID);
        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(messageSourceMock);
    }

    @Test
    public void findAll() {
        BindingAwareModelMap model = new BindingAwareModelMap();

        List<StoryGroup> models = new ArrayList<StoryGroup>();
        when(serviceMock.findAll()).thenReturn(models);

        String view = controller.findAll(model);

        verify(serviceMock, times(1)).findAll();
        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(messageSourceMock);

        assertEquals(StoryGroupController.VIEW_LIST, view);
        assertEquals(models, model.asMap().get(StoryGroupController.MODEL_ATTRIBUTE_LIST));
    }

    @Test
    public void findById() throws NotFoundException {
        BindingAwareModelMap model = new BindingAwareModelMap();

        StoryGroup found = StoryGroupTestUtil.createModel(StoryGroupTestUtil.ID, StoryGroupTestUtil.DESCRIPTION, StoryGroupTestUtil.TITLE);
        when(serviceMock.findById(StoryGroupTestUtil.ID)).thenReturn(found);

        String view = controller.findById(StoryGroupTestUtil.ID, model);

        verify(serviceMock, times(1)).findById(StoryGroupTestUtil.ID);
        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(messageSourceMock);

        assertEquals(StoryGroupController.VIEW_VIEW, view);
        assertEquals(found, model.asMap().get(StoryGroupController.MODEL_ATTRIBUTE));
    }

    @Test(expected = NotFoundException.class)
    public void findByIdWhenIsNotFound() throws NotFoundException {
        BindingAwareModelMap model = new BindingAwareModelMap();

        when(serviceMock.findById(StoryGroupTestUtil.ID)).thenThrow(new NotFoundException(""));

        controller.findById(StoryGroupTestUtil.ID, model);

        verify(serviceMock, times(1)).findById(StoryGroupTestUtil.ID);
        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(messageSourceMock);
    }

    @Test
    public void showUpdateStoryGroupForm() throws NotFoundException {
        BindingAwareModelMap model = new BindingAwareModelMap();

        StoryGroup updated = StoryGroupTestUtil.createModel(StoryGroupTestUtil.ID, StoryGroupTestUtil.DESCRIPTION, StoryGroupTestUtil.TITLE);
        when(serviceMock.findById(StoryGroupTestUtil.ID)).thenReturn(updated);

        String view = controller.showUpdateForm(StoryGroupTestUtil.ID, model);

        verify(serviceMock, times(1)).findById(StoryGroupTestUtil.ID);
        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(messageSourceMock);

        assertEquals(StoryGroupController.VIEW_UPDATE, view);

        StoryGroupDTO formObject = (StoryGroupDTO) model.asMap().get(StoryGroupController.MODEL_ATTRIBUTE);

        assertEquals(updated.getId(), formObject.getId());
        assertEquals(updated.getDescription(), formObject.getDescription());
        assertEquals(updated.getTitle(), formObject.getTitle());
    }

    @Test(expected = NotFoundException.class)
    public void showUpdateStoryGroupFormWhenIsNotFound() throws NotFoundException {
        BindingAwareModelMap model = new BindingAwareModelMap();

        when(serviceMock.findById(StoryGroupTestUtil.ID)).thenThrow(new NotFoundException(""));

        controller.showUpdateForm(StoryGroupTestUtil.ID, model);

        verify(serviceMock, times(1)).findById(StoryGroupTestUtil.ID);
        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(messageSourceMock);
    }

    @Test
    public void update() throws NotFoundException {
        StoryGroupDTO formObject = StoryGroupTestUtil.createFormObject(StoryGroupTestUtil.ID, StoryGroupTestUtil.DESCRIPTION_UPDATED, StoryGroupTestUtil.TITLE_UPDATED);

        StoryGroup model = StoryGroupTestUtil.createModel(StoryGroupTestUtil.ID, StoryGroupTestUtil.DESCRIPTION_UPDATED, StoryGroupTestUtil.TITLE_UPDATED);
        when(serviceMock.update(formObject)).thenReturn(model);

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/storygroup/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        initMessageSourceForFeedbackMessage(StoryGroupController.FEEDBACK_MESSAGE_KEY_UPDATED);

        String view = controller.update(formObject, result, attributes);

        verify(serviceMock, times(1)).update(formObject);
        verifyNoMoreInteractions(serviceMock);

        String expectedView = StoryGroupTestUtil.createRedirectViewPath(StoryGroupController.REQUEST_MAPPING_VIEW);
        assertEquals(expectedView, view);

        assertEquals(Long.valueOf((String) attributes.get(StoryGroupController.PARAMETER_ID)), model.getId());

        assertFeedbackMessage(attributes, StoryGroupController.FEEDBACK_MESSAGE_KEY_UPDATED);
    }

    @Test
    public void updateEmpty() throws NotFoundException {
        StoryGroupDTO formObject = StoryGroupTestUtil.createFormObject(StoryGroupTestUtil.ID, "", "");

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/storygroup/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        String view = controller.update(formObject, result, attributes);

        verifyZeroInteractions(messageSourceMock, serviceMock);

        assertEquals(StoryGroupController.VIEW_UPDATE, view);
        assertFieldErrors(result, FIELD_TITLE);
    }

    @Test
    public void updateWhenDescriptionAndTitleAreTooLong() throws NotFoundException {
        String description = StoryGroupTestUtil.createStringWithLength(StoryGroup.MAX_LENGTH_DESCRIPTION + 1);
        String title = StoryGroupTestUtil.createStringWithLength(StoryGroup.MAX_LENGTH_TITLE + 1);

        StoryGroupDTO formObject = StoryGroupTestUtil.createFormObject(StoryGroupTestUtil.ID, description, title);

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/storygroup/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        String view = controller.update(formObject, result, attributes);

        verifyZeroInteractions(messageSourceMock, serviceMock);

        assertEquals(StoryGroupController.VIEW_UPDATE, view);
        assertFieldErrors(result, FIELD_DESCRIPTION, FIELD_TITLE);
    }

    @Test(expected = NotFoundException.class)
    public void updateWhenIsNotFound() throws NotFoundException {
        StoryGroupDTO formObject = StoryGroupTestUtil.createFormObject(StoryGroupTestUtil.ID, StoryGroupTestUtil.DESCRIPTION_UPDATED, StoryGroupTestUtil.TITLE_UPDATED);

        when(serviceMock.update(formObject)).thenThrow(new NotFoundException(""));

        MockHttpServletRequest mockRequest = new MockHttpServletRequest("POST", "/storygroup/add");
        BindingResult result = bindAndValidate(mockRequest, formObject);

        RedirectAttributesModelMap attributes = new RedirectAttributesModelMap();

        controller.update(formObject, result, attributes);

        verify(serviceMock, times(1)).update(formObject);
        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(messageSourceMock);
    }

    private void assertFeedbackMessage(RedirectAttributes attributes, String messageCode) {
        assertFlashMessages(attributes, messageCode, StoryGroupController.FLASH_MESSAGE_KEY_FEEDBACK);
    }

    private void assertFieldErrors(BindingResult result, String... fieldNames) {
        assertEquals(fieldNames.length, result.getFieldErrorCount());
        for (String fieldName : fieldNames) {
            assertNotNull(result.getFieldError(fieldName));
        }
    }

    private void assertFlashMessages(RedirectAttributes attributes, String messageCode, String flashMessageParameterName) {
        Map<String, ?> flashMessages = attributes.getFlashAttributes();
        Object message = flashMessages.get(flashMessageParameterName);

        assertNotNull(message);
        flashMessages.remove(message);
        assertTrue(flashMessages.isEmpty());

        verify(messageSourceMock, times(1)).getMessage(eq(messageCode), any(Object[].class), any(Locale.class));
        verifyNoMoreInteractions(messageSourceMock);
    }

    private BindingResult bindAndValidate(HttpServletRequest request, Object formObject) {
        WebDataBinder binder = new WebDataBinder(formObject);
        binder.setValidator(validator);
        binder.bind(new MutablePropertyValues(request.getParameterMap()));
        binder.getValidator().validate(binder.getTarget(), binder.getBindingResult());
        return binder.getBindingResult();
    }

    private void initMessageSourceForFeedbackMessage(String feedbackMessageCode) {
        when(messageSourceMock.getMessage(eq(feedbackMessageCode), any(Object[].class), any(Locale.class))).thenReturn(FEEDBACK_MESSAGE);
    }
}
