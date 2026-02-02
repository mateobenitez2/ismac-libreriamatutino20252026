import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Autor } from '../model/autor.model';

@Injectable({
  providedIn: 'root',
})
export class AutorService {

  private baseURL = "http://localhost:8080/api/autor"

  constructor(private http: HttpClient){ }

    findAll(): Observable<Autor[]>{
      return this.http.get<Autor[]>(this.baseURL);
    }

    findOne(id: number): Observable<Autor>{
      return this.http.get<Autor>(`${this.baseURL}/${id}`);
    }

    save(autor: Autor): Observable<Autor>{
      return this.http.post<Autor>(this.baseURL, autor);
    } 

    update(id: number, autor: Autor): Observable<Autor>{
      return this.http.put<Autor>(`${this.baseURL}/${id}`, autor);
    } 

    delete(id: number): Observable<void>{
      return this.http.delete<void>(`${this.baseURL}/${id}`);
    }
}
