import { MigrationInterface, QueryRunner } from "typeorm";

export class CategoryValues1747233494464 implements MigrationInterface {

    public async up(queryRunner: QueryRunner): Promise<void> {
        await queryRunner.query(
            `
                INSERT INTO categories(name)
                VALUES ('Important'), ('Urgent'), ('Regular');
            `
        )
    }

    public async down(queryRunner: QueryRunner): Promise<void> {
        `
            DELETE FROM categories
        `
    }

}
