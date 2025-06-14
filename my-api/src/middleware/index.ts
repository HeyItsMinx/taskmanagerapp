import { Request, Response, NextFunction } from "express";

export class AppError extends Error {
    status?: number;
}

export const errorHandler = (
    err: AppError,
    req: Request,
    res: Response,
    next: NextFunction
) => {
    console.error(err);
    res.status(err.status || 500).json({
        error: err.message || "Internal Server Error",
    });
};
