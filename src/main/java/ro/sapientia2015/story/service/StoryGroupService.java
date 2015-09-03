package ro.sapientia2015.story.service;

import java.util.List;

import ro.sapientia2015.story.dto.StoryDTO;
import ro.sapientia2015.story.dto.StoryGroupDTO;
import ro.sapientia2015.story.exception.NotFoundException;
import ro.sapientia2015.story.model.Story;
import ro.sapientia2015.story.model.StoryGroup;

/**
 * @author Kiss Tibor
 */
public interface StoryGroupService {

    /**
     * Adds a new to-do entry.
     * @param added The information of the added to-do entry.
     * @return  The added to-do entry.
     */
    public StoryGroup add(StoryGroupDTO added);

    /**
     * Deletes a to-do entry.
     * @param id    The id of the deleted to-do entry.
     * @return  The deleted to-do entry.
     * @throws NotFoundException    if no to-do entry is found with the given id.
     */
    public StoryGroup deleteById(Long id) throws NotFoundException;

    /**
     * Returns a list of to-do entries.
     * @return
     */
    public List<StoryGroup> findAll();

    /**
     * Finds a to-do entry.
     * @param id    The id of the wanted to-do entry.
     * @return  The found to-entry.
     * @throws NotFoundException    if no to-do entry is found with the given id.
     */
    public StoryGroup findById(Long id) throws NotFoundException;

    /**
     * Updates the information of a to-do entry.
     * @param updated   The information of the updated to-do entry.
     * @return  The updated to-do entry.
     * @throws NotFoundException    If no to-do entry is found with the given id.
     */
    public StoryGroup update(StoryGroupDTO updated) throws NotFoundException;
}
