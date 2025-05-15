import { Entity, PrimaryGeneratedColumn, Column, UpdateDateColumn, ManyToOne, JoinColumn } from "typeorm"
import { Category } from "./category.entity";

@Entity({name: 'tasks'})
export class Task {
    @PrimaryGeneratedColumn()
    id:number;

    @Column()
    title:string;

    @Column()
    description:string;

    @UpdateDateColumn({ name: 'updated_at', type: 'timestamp', default: () => 'CURRENT_TIMESTAMP' })
    updated_at: Date;

    @Column()
    done:boolean;

    @ManyToOne(() => Category, (category) => category.tasks)
    @JoinColumn({name: 'category_id'})
    category: Category;
}
