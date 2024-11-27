package org.finlelab.beko.repository;

import org.finlelab.beko.entity.Category;
import org.finlelab.beko.entity.Task;
import org.finlelab.beko.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    // Пагинация с фильтрацией по имени пользователя, категории и статусу
    Page<Task> findByUserUsernameAndCategory_CategoryIdAndStatus(String username, Long categoryId, String status, Pageable pageable);
    Page<Task> findByUserUsernameAndCategory_CategoryId(String username, Long categoryId, Pageable pageable);
    Page<Task> findByUserUsernameAndStatus(String username, String status, Pageable pageable);
    Page<Task> findByUserUsername(String username, Pageable pageable);

    Page<Task> findByUserUsernameAndTitleContainingOrUserUsernameAndDescriptionContaining(String username, String title, String username2, String description, Pageable pageable);

//    List<Task> findByUserUsernameAndCategory_CategoryIdAndStatusOrderByDueDateAsc(String username, Long categoryId, String status);
//
//    List<Task> findByUserUsernameAndCategory_CategoryIdOrderByDueDateAsc(String username, Long categoryId);
//
//    List<Task> findByUserUsernameAndStatusOrderByDueDateAsc(String username, String status);
//
//    List<Task> findByUserUsernameOrderByDueDateAsc(String username);
//
//    List<Task> findByUserUsernameAndCategory_CategoryIdAndStatusOrderByDueDateDesc(String username, Long categoryId, String status);
//
//    List<Task> findByUserUsernameAndCategory_CategoryIdOrderByDueDateDesc(String username, Long categoryId);
//
//    List<Task> findByUserUsernameAndStatusOrderByDueDateDesc(String username, String status);
//
//    List<Task> findByUserUsernameOrderByDueDateDesc(String username);

    Task findByTaskIdAndUserUsername(Long taskId, String username);
}
