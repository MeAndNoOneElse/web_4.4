import { Component, Input, Output, EventEmitter, OnInit, ViewChild, ElementRef, AfterViewInit, OnChanges } from '@angular/core';
import { CommonModule } from '@angular/common';

interface PointResult {
  id: number;
  x: number;
  y: number;
  r: number;
  hit: boolean;
  createdAt: string;
}

@Component({
  selector: 'app-graph',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './graph.component.html',
  styleUrl: './graph.component.css'
})
export class GraphComponent implements OnInit, AfterViewInit, OnChanges {
  @Input() results: PointResult[] = [];
  @Input() radius: number | null = null;
  @Output() pointClicked = new EventEmitter<{ x: number; y: number }>();

  @ViewChild('graphCanvas', { static: false }) canvasRef!: ElementRef<HTMLCanvasElement>;

  private canvas!: HTMLCanvasElement;
  private ctx!: CanvasRenderingContext2D;
  private scale = 50; // пиксели на единицу
  private centerX = 200;
  private centerY = 200;

  ngOnInit() {
    setTimeout(() => this.drawGraph(), 0);
  }

  ngAfterViewInit() {
    this.canvas = this.canvasRef.nativeElement;
    this.ctx = this.canvas.getContext('2d')!;
    this.drawGraph();
  }

  ngOnChanges() {
    if (this.ctx) {
      this.drawGraph();
    }
  }

  drawGraph() {
    if (!this.ctx) return;

    const w = this.canvas.width;
    const h = this.canvas.height;

    // Очистка
    this.ctx.fillStyle = '#fff';
    this.ctx.fillRect(0, 0, w, h);

    // Оси
    this.ctx.strokeStyle = '#000';
    this.ctx.lineWidth = 2;
    this.ctx.beginPath();
    this.ctx.moveTo(this.centerX, 0);
    this.ctx.lineTo(this.centerX, h);
    this.ctx.moveTo(0, this.centerY);
    this.ctx.lineTo(w, this.centerY);
    this.ctx.stroke();

    // Делений и значений
    this.ctx.font = '12px Arial';
    this.ctx.fillStyle = '#000';
    for (let i = -3; i <= 5; i++) {
      const x = this.centerX + i * this.scale;
      this.ctx.fillText(i.toString(), x - 5, this.centerY + 15);

      const y = this.centerY - i * this.scale;
      this.ctx.fillText(i.toString(), this.centerX - 25, y + 5);
    }

    // Рисование области (пример для варианта 1 - сектор в первой четверти)
    // Это нужно настроить под свой вариант
    if (this.radius !== null && this.radius > 0) {
      this.drawArea(this.radius);
    }

    // Рисование точек результатов
    this.results.forEach(result => {
      const canvasX = this.centerX + result.x * this.scale;
      const canvasY = this.centerY - result.y * this.scale;

      this.ctx.fillStyle = result.hit ? '#4CAF50' : '#f44336';
      this.ctx.beginPath();
      this.ctx.arc(canvasX, canvasY, 5, 0, 2 * Math.PI);
      this.ctx.fill();
    });

    // Граница
    this.ctx.strokeStyle = '#ccc';
    this.ctx.lineWidth = 1;
    this.ctx.strokeRect(0, 0, w, h);
  }

  drawArea(r: number) {
    // Пример: сектор в первой четверти с радиусом r
    const radius = r * this.scale;

    this.ctx.fillStyle = 'rgba(100, 150, 255, 0.2)';
    this.ctx.beginPath();
    this.ctx.moveTo(this.centerX, this.centerY);
    this.ctx.arc(this.centerX, this.centerY, radius, -Math.PI / 2, 0);
    this.ctx.closePath();
    this.ctx.fill();

    this.ctx.strokeStyle = 'rgba(100, 150, 255, 0.8)';
    this.ctx.lineWidth = 2;
    this.ctx.stroke();
  }

  onCanvasClick(event: MouseEvent) {
    const rect = this.canvas.getBoundingClientRect();
    const canvasX = event.clientX - rect.left;
    const canvasY = event.clientY - rect.top;

    const x = Math.round((canvasX - this.centerX) / this.scale);
    const y = Math.round((this.centerY - canvasY) / this.scale);

    this.pointClicked.emit({ x, y });
  }
}
