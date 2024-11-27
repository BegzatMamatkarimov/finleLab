package org.finlelab.beko.controller;

import jakarta.validation.Valid;
import org.finlelab.beko.dto.TaskDTO;
import org.finlelab.beko.entity.Category;
import org.finlelab.beko.entity.Task;
import org.finlelab.beko.entity.User;
import org.finlelab.beko.service.CategoryService;
import org.finlelab.beko.service.TaskService;
import org.finlelab.beko.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;



@Controller
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;
    private final CategoryService categoryService;
    private final UserService userService;

    public TaskController(TaskService taskService, CategoryService categoryService, UserService userService) {
        this.taskService = taskService;
        this.categoryService = categoryService;
        this.userService = userService;
    }


    @GetMapping
    public String listTasks(Model model, Authentication authentication,
                            @RequestParam(required = false) Long category,
                            @RequestParam(required = false) String status,
                            @RequestParam(required = false) String sortOrder,
                            @RequestParam(required = false) String searchQuery,
                            @RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "5") int size) {

        String username = authentication.getName();

        // Получаем задачи с пагинацией и фильтрацией
        Page<Task> taskPage = taskService.getUserTasks(username, category, status, sortOrder, searchQuery, page, size);

        // Добавляем в модель
        model.addAttribute("tasks", taskPage.getContent());
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("sortOrder", sortOrder);
        model.addAttribute("page", taskPage);
        model.addAttribute("searchQuery", searchQuery);
        model.addAttribute("lastPage", taskPage.getTotalPages() - 1);

        return "task-list";
    }


    // Показ формы для создания новой задачи
    @GetMapping("/create")
    public String createTaskForm(Model model, Authentication authentication) {
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        Task task = new Task();
        task.setUser(user);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("task", new Task());

        return "task-form";
    }

    // Сохранение задачи (создание/редактирование)
    @PostMapping("/save")
    public String saveTask(@ModelAttribute("task") Task task, Authentication authentication) {
        // Присваиваем пользователю задачу
        String username = authentication.getName();
        User user = userService.findByUsername(username);

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        task.setUser(user);
        if (task.getCategory() == null) {
            Category defaultCategory = categoryService.findById(1L);
            task.setCategory(defaultCategory);
        }

        taskService.saveTask(task, username);
        return "redirect:/tasks";
    }

    @GetMapping("/edit/{taskId}")
    public String editTaskForm(@PathVariable Long taskId, Model model, Authentication authentication) {
        String username = authentication.getName();
        Task task = taskService.getTaskByIdAndUsername(taskId, username);
        if (task == null) {
            return "redirect:/tasks";
        }
        model.addAttribute("task", task);
        model.addAttribute("categories", categoryService.findAll());
        return "task-form";
    }
// Удаление задачи
    @GetMapping("/delete/{taskId}")
    public String deleteTask(@PathVariable Long taskId, Authentication authentication) {
        String username = authentication.getName();
        taskService.deleteTask(taskId, username);
        return "redirect:/tasks";
    }
}

