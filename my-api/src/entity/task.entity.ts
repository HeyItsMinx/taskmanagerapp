import { Entity, PrimaryGeneratedColumn, Column, UpdateDateColumn, ManyToOne } from "typeorm"
import { Category } from "./category.entity";

@Entity()
export class Task {
    @PrimaryGeneratedColumn()
    id: number

    @Column()
    title:string

    @UpdateDateColumn({ name: 'updated_at' })
    updatedAt: Date;

    @Column()
    done:boolean

    @ManyToOne(() => Category, (category) => category.tasks)
    category: Category
}
