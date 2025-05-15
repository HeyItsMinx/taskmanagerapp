import { NextFunction, Request, Response } from "express";
import { tasksRepository } from "../repository";

export class TaskController {
    static async getAllTasks(req: Request, res: Response, next: NextFunction) {
        try {
            
            const category = (typeof req.query.category === "string") ? req.query.category : null;
            const doneString = (["true", "false"].includes(req.query.done as string)) ? req.query.done : null;
            const done = doneString ? (doneString === "true") : null
    
            const tasks = await tasksRepository.find({
                select: {category:{name:true}},
                relations: {category: true},
                where: {
                    category: {name: category},
                    done
                }
            });
            
            res.status(200).json({
                data: tasks
            })
        } catch (error) {
            next(error)
        }
    }

    static async getTaskById(req: Request, res: Response, next: NextFunction) {
        try {
            const id = parseInt(req.params.id);
            const tasks = await tasksRepository.findOne({
                select: {category:{name:true}},
                where: {id},
                relations: {category: true}
            });
            res.status(200).json({
                data: tasks
            })
        } catch (error) {
            next(error)
        }
    }

    static async createTask(req: Request, res: Response, next: NextFunction) {
        try {
            const { title, description, category_id } = req.body;
            const task = tasksRepository.create({
                title: title,
                description: description,
                category: {id: category_id}
            });
            

            const savedTask = await tasksRepository.save(task);

            res.status(201).json({
                data: {id: savedTask.id},
                message: "Your task has been created!"
            });
        } catch (error) {
            next(error);
        }
    }

    static async updateTask(req: Request, res: Response, next: NextFunction) {
        try {
            const id = parseInt(req.params.id);
            
            const { title, description, category_id, done } = req.body;

            const task = await tasksRepository.findOne({
                where: {id},
                relations: {category:true}
            });

            if (!task) {
                res.status(404).json({
                    error: "task not found"
                });
            }

            task.title = title;
            task.description = description;
            task.category.id = category_id;
            task.done = done;
            
            const updatedTask = await tasksRepository.save(task);

            res.status(200).json({
                data: {...updatedTask},
                message: "Your task has been updated!"
            });
        } catch (error) {
            next(error);
        }
    }

    static async deleteTask(req: Request, res: Response, next: NextFunction) {
        try {
            const id = parseInt(req.params.id);

            const task = await tasksRepository.findOne({
                where: {id}
            });

            if (!task) {
                res.status(404).json({
                    error: "task not found"
                });
            }
            
            await tasksRepository.remove(task);

            res.status(200).json({
                message: "Your task has been removed!"
            });
        } catch (error) {
            next(error);
        }
    }

    static async finishTask(req: Request, res: Response, next: NextFunction) {
        try {
            const id = parseInt(req.params.id);
            
            const task = await tasksRepository.findOne({
                where: {id},
                relations: {category:true}
            });

            if (!task) {
                res.status(404).json({
                    error: "task not found"
                });
            }

            task.done = true;
            
            await tasksRepository.save(task);

            res.status(200).json({
                message: "Your task has been marked as done!"
            });
        } catch (error) {
            next(error);
        }
    }
}