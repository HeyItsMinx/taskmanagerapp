import { AppDataSource } from "./data-source";
import * as express from "express";
import * as dotenv from "dotenv";
import * as morgan from "morgan";

import "reflect-metadata";
import { taskRoute } from "./route/task.route";
import { errorHandler } from "./middleware";
import { categoryRoute } from "./route/category.route";
dotenv.config();

const app = express();
const { PORT = 3000 } = process.env;
const PREFIX = "/api";
app.use(express.json());
app.use(morgan("combined"));

// routes
app.use(`${PREFIX}/task`, taskRoute);
app.use(`${PREFIX}/category`, categoryRoute);


app.use(errorHandler);

AppDataSource.initialize()
  .then(async () => {
    app.listen(PORT, () => {
      console.log("Server is running on http://localhost:" + PORT);
    });
    console.log("Data Source has been initialized!");
  })
  .catch((error) => console.log(error));