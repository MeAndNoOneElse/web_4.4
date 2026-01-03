import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface PointRequest {
  x: number;
  y: number;
  r: number;
}

export interface ResultResponse {
  id: number;
  x: number;
  y: number;
  r: number;
  hit: boolean;
  createdAt: string;
}

@Injectable({
  providedIn: 'root'
})
export class ResultService {

  private baseUrl = 'http://localhost:8080/lab4/results';

  constructor(private http: HttpClient) {}

  checkPoint(body: PointRequest): Observable<ResultResponse> {
    return this.http.post<ResultResponse>(`${this.baseUrl}/check`, body);
  }

  getResults(): Observable<ResultResponse[]> {
    return this.http.get<ResultResponse[]>(this.baseUrl);
  }

  clearResults(): Observable<void> {
    return this.http.delete<void>(this.baseUrl);
  }
}
