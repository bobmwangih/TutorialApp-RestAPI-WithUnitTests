package com.bob.test.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.bob.model.Tutorial;
import com.bob.repository.TutorialRepository;

@DataJpaTest
public class TutorialRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private TutorialRepository repository;

	@Test
	public void should_find_no_tutorials_if_repository_is_empty() {
		Iterable<Tutorial> tutorials = repository.findAll();
		assertThat(tutorials).isEmpty();
	}

	@Test
	public void should_store_a_tutorial() {
		Tutorial tutorial = repository.save(new Tutorial("tutorial 1", "tutorial desc", true));
		assertThat(tutorial).hasFieldOrPropertyWithValue("title", "tutorial 1");
		assertThat(tutorial).hasFieldOrPropertyWithValue("description", "tutorial desc");
		assertThat(tutorial).hasFieldOrPropertyWithValue("published", true);
	}

	@Test
	public void should_find_all_tutorials() {
		Tutorial tut1 = new Tutorial("tutorial 1", "tutorial desc1", true);
		entityManager.persist(tut1);
		Tutorial tut2 = new Tutorial("tutorial 2", "tutorial desc2", false);
		entityManager.persist(tut2);
		Tutorial tut3 = new Tutorial("tutorial 3", "tutorial desc3", true);
		entityManager.persist(tut3);
		Iterable<Tutorial> tutorials = repository.findAll();
		assertThat(tutorials).hasSize(3).contains(tut1, tut2, tut3);
	}

	@Test
	public void should_find_tutorial_by_id() {
		Tutorial tutorial = new Tutorial("tutorial 1", "tutorial desc", true);
		entityManager.persist(tutorial);
		Tutorial foundTutorial = repository.findById(tutorial.getId()).get();
		assertThat(foundTutorial).isEqualTo(tutorial);
	}

	@Test
	public void should_find_published_tutorials() {
		Tutorial tut1 = new Tutorial("tutorial 1", "tutorial desc1", true);
		entityManager.persist(tut1);
		Tutorial tut2 = new Tutorial("tutorial 2", "tutorial desc2", false);
		entityManager.persist(tut2);
		Tutorial tut3 = new Tutorial("tutorial 3", "tutorial desc3", true);
		entityManager.persist(tut3);
		Iterable<Tutorial> publishedTutorials = repository.findByPublished(true);
		assertThat(publishedTutorials).hasSize(2).contains(tut1, tut3);
	}

	@Test
	public void should_find_tutorials_by_title_containing_string() {
		Tutorial tut1 = new Tutorial("test 1", "tutorial desc1", true);
		entityManager.persist(tut1);
		Tutorial tut2 = new Tutorial("tutorial 2", "tutorial desc2", false);
		entityManager.persist(tut2);
		Tutorial tut3 = new Tutorial("tutorial 3", "tutorial desc3", true);
		entityManager.persist(tut3);
		Iterable<Tutorial> tutorials = repository.findByTitleContaining("tutorial");
		assertThat(tutorials).hasSize(2).contains(tut2, tut3);
	}

	@Test
	public void should_update_tutorial_by_id() {
		Tutorial tut1 = new Tutorial("test 1", "tutorial desc1", true);
		entityManager.persist(tut1);
		Tutorial tutorialToBeUpdated = new Tutorial("tutorial 1", "tutorial desc1", false);
		Tutorial savedTutorial = repository.findById(tut1.getId()).get();
		Tutorial updatedTutorial = repository.save(new Tutorial());
	}

	@Test
	public void should_delete_tutorial_by_id() {
		Tutorial tut1 = new Tutorial("test 1", "tutorial desc1", true);
		entityManager.persist(tut1);
		Tutorial tut2 = new Tutorial("tutorial 2", "tutorial desc2", false);
		entityManager.persist(tut2);
		Tutorial tut3 = new Tutorial("tutorial 3", "tutorial desc3", true);
		entityManager.persist(tut3);
		repository.deleteById(tut2.getId());
		Iterable<Tutorial> tutorials = repository.findAll();
		assertThat(tutorials).hasSize(2).contains(tut1, tut3);
	}

	@Test
	public void should_delete_all_tutorials() {
		Tutorial tut1 = new Tutorial("test 1", "tutorial desc1", true);
		entityManager.persist(tut1);
		Tutorial tut2 = new Tutorial("tutorial 2", "tutorial desc2", false);
		entityManager.persist(tut2);
		Tutorial tut3 = new Tutorial("tutorial 3", "tutorial desc3", true);
		entityManager.persist(tut3);
		repository.deleteAll();
		Iterable<Tutorial> tutorials = repository.findAll();
		assertThat(tutorials).isEmpty();
	}
}
