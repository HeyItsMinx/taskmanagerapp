import { Router } from "express";
import { CategoryController } from "../controller/category.controller";

const router = Router();

router.get('/', CategoryController.getCategories);

export {router as categoryRoute};