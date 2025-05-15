
import { Router } from "express";
import { TaskController } from "../controller/task.controller";

const router = Router();

router.get('/', TaskController.getAllTasks);
router.get('/:id', TaskController.getTaskById);
router.post('/', TaskController.createTask);
router.put('/done/:id', TaskController.finishTask);
router.put('/:id', TaskController.updateTask);
router.delete('/:id', TaskController.deleteTask);

export { router as taskRoute };
