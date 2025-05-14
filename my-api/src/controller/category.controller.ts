import { Request, Response, NextFunction } from "express";
import { categoriesRepository } from "../repository";

export class CategoryController {
    static async getCategories(req:Request, res:Response, next: NextFunction) {
        try {
            const data = await categoriesRepository.find();
            res.status(200).json({
                data
            });
        } catch (error) {
            next(error)
        }
    }
}