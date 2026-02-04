import { Component, ElementRef, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { Libro } from '../../model/libro.model';
import { Autor } from '../../model/autor.model';
import { Categoria } from '../../model/categoria.model';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { LibroService } from '../../services/libro';
import { AutorService } from '../../services/autor';
import { CategoriaService } from '../../services/categoria';
import { HttpClient } from '@angular/common/http';
import { MatDialog } from '@angular/material/dialog';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-libro',
  standalone: false,
  templateUrl: './libro.html',
  styleUrl: './libro.css',
})
export class LibroComponent implements OnInit {

  libros: Libro[] = [];
  autores: Autor[] = [];
  categorias: Categoria[] = [];
  libro: Libro = { } as Libro;
  editar: boolean = false;
  idEditar: number | null = null;
  dataSource!: MatTableDataSource<Libro>;
  seleccionarArchivo!: File;
  imagenAnterior: string = "";

  mostrarColumnas: string[] = ['idLibro', 'titulo', 'editorial', 'edicion', 'idioma', 'fechapublicacion', 'numEjemplares', 'precio', 'autor', 'categoria', 'acciones'];

  @ViewChild('formularioLibro') formulariolibro!: ElementRef;
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;
  @ViewChild('modalLibro') modalLibro!: TemplateRef<any>;

  constructor(
    private libroService: LibroService,
    private autorService: AutorService,
    private categoriaService: CategoriaService,
    private dialog: MatDialog,
    private http: HttpClient
  ){}

  ngOnInit(): void {
    this.findAll();
    this.cargarAutores();
    this.cargarCategorias();
  }

  findAll(): void{
    this.libroService.findAll().subscribe(data => {
      this.dataSource = new MatTableDataSource(data);
      this.dataSource.paginator = this.paginator;
      this.dataSource.sort = this.sort;
    });
  }

  cargarAutores(): void{
    this.autorService.findAll().subscribe(data => {
      this.autores = data;
    })
  }

  cargarCategorias(): void{
    this.categoriaService.findAll().subscribe(data => {
      this.categorias = data;
    })
  }

  save(): void{
    this.libroService.save(this.libro).subscribe(() => {
      this.libro = { } as Libro;
      this.findAll();
    });
  }

  update(): void{
    if(this.idEditar!== null){
      this.libroService.update(this.idEditar, this.libro).subscribe(() =>{
        this.libro = { } as Libro;
        this.editar = false;
        this.idEditar = null;
        this.findAll();
      });
    }
  }

  delete(): void{
    Swal.fire({ 
      title: '¿Desea eliminar el libro?',
      text: 'Esta accion no se puede deshacer',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Si, eliminar',
      cancelButtonText: 'Cancelar',
      confirmButtonColor: '#d33',
      cancelButtonColor: '#3085d6'
     }).then((result) => { });
  }

}
