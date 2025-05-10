import { Entity, PrimaryGeneratedColumn, Column, UpdateDateColumn, ManyToOne, OneToMany } from "typeorm"
import { Task } from "./task.entity"

@Entity()
export class Category {
    @PrimaryGeneratedColumn()
    id: number

    @Column()
    name:string

    @OneToMany(() => Task, (task) => task.category)
    tasks: Task[]
}
