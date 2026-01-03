import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  FormsModule,
  ReactiveFormsModule,
  FormBuilder,
  FormGroup,
  Validators
} from '@angular/forms';
import { GraphComponent } from '../graph/graph.component';
import { ResultsTableComponent } from '../results-table/results-table.component';
import {
  ResultService,
  ResultResponse
} from '../../services/result.service';

@Component({
  selector: 'app-main-page',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule, GraphComponent, ResultsTableComponent],
  templateUrl: './main-page.component.html',
  styleUrls: ['./main-page.component.css']
})
export class MainPageComponent implements OnInit {

  studentName = 'Иван Иванов';
  groupNumber = 'P3210';
  variant = '1';

  form!: FormGroup;
  results: ResultResponse[] = [];

  xOptions = [-3, -2, -1, 0, 1, 2, 3, 4, 5];
  rOptions = [-3, -2, -1, 0, 1, 2, 3, 4, 5];

  // можно привязать к графику, если нужно
  get selectedR(): number | null {
    return this.form?.value?.r ?? null;
  }

  constructor(
    private fb: FormBuilder,
    private resultService: ResultService
  ) {}

  ngOnInit(): void {
    this.form = this.fb.group({
      x: [null, Validators.required],
      y: [0, [Validators.required, Validators.min(-3), Validators.max(3)]],
      r: [null, Validators.required]
    });

    this.loadResults();
  }

  loadResults(): void {
    this.resultService.getResults().subscribe((res: ResultResponse[]) => {
      this.results = res;
    });
  }

  onSubmit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const { x, y, r } = this.form.value;

    this.resultService.checkPoint({ x, y, r }).subscribe((result: ResultResponse) => {
      // добавляем новый результат наверх списка
      this.results.unshift(result);
    });
  }

  onPointClicked(coords: { x: number; y: number }): void {
    // клик по графику подставляет X и Y в форму
    this.form.patchValue({
      x: coords.x,
      y: coords.y
    });
  }

  onClearResults(): void {
    this.resultService.clearResults().subscribe(() => {
      this.results = [];
    });
  }

  logout(): void {
    // пока без авторизации: просто перезагрузка
    window.location.reload();
  }
}
