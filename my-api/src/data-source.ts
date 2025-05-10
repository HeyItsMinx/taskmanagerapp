import "reflect-metadata"
import { DataSource } from "typeorm"
import { Task } from "./entity/task.entity"
import { Category } from "./entity/category.entity";
import * as dotenv from "dotenv";

dotenv.config();

const {
    MYSQL_USERNAME, MYSQL_PASSWORD, MYSQL_DBNAME, MYSQL_PORT, MYSQL_HOST,
} = process.env;

export const AppDataSource = new DataSource({
    type: "mysql",
    host: MYSQL_DBNAME,
    port: parseInt(MYSQL_PORT || "3306"),
    username: MYSQL_USERNAME,
    password: MYSQL_PASSWORD,
    database: MYSQL_DBNAME,
    synchronize: true,
    logging: false,
    entities: [Task, Category],
    migrations: [__dirname + "/migration/*.ts"],
    subscribers: [],
})
