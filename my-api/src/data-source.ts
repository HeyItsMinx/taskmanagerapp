import "reflect-metadata"
import { DataSource } from "typeorm"
import * as dotenv from "dotenv";
import { Category, Task } from "./entity";

dotenv.config();

const {
    MYSQL_USERNAME, MYSQL_PASSWORD, MYSQL_DBNAME, MYSQL_PORT, MYSQL_HOST,
} = process.env;

export const AppDataSource = new DataSource({
    type: "mysql",
    host: MYSQL_HOST || "localhost",
    port: parseInt(MYSQL_PORT || "3306"),
    username: MYSQL_USERNAME,
    password: MYSQL_PASSWORD,
    database: MYSQL_DBNAME || "tmdb",
    synchronize: false,
    logging: false,
    entities: [Task, Category],
    migrations: [__dirname + "/migration/*.ts"],
    subscribers: [],
})
