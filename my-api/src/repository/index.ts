import { AppDataSource } from "../data-source";
import { Category } from "../entity/category.entity";
import { Task } from "../entity/task.entity";

export const tasksRepository = AppDataSource.getRepository(Task)
export const categoriesRepository = AppDataSource.getRepository(Category)