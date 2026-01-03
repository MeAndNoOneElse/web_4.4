import { Component, Input } from '@angular/core';
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
  selector: 'app-results-table',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './results-table.component.html',
  styleUrl: './results-table.component.css'
})
export class ResultsTableComponent {
  @Input() results: PointResult[] = [];
}
