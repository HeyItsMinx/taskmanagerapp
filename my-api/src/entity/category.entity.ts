import { Entity, PrimaryGeneratedColumn, Column, UpdateDateColumn, ManyToOne, OneToMany } from "typeorm"
import { Task } from "./task.entity"

@Entity({name: 'categories'})
export class Category {
    @PrimaryGeneratedColumn()
    id: number

    @Column()
    name:string

    @OneToMany(() => Task, (task) => task.category)
    tasks: Task[]
}
