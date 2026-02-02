import { Component, ElementRef, OnInit, ViewChild } from "@angular/core";
import { MatPaginator } from "@angular/material/paginator";
import { MatSort } from "@angular/material/sort";
import { Categoria } from "../../model/categoria.model";
import { MatTableDataSource } from "@angular/material/table";
import { CategoriaService } from "../../services/categoria";
import Swal from "sweetalert2";
import { NgForm } from "@angular/forms";

@Component({
    selector: 'app-categoria',
    standalone: false,
    templateUrl: './categoria.html',
    styleUrl: './categoria.css',
})
export class CategoriaComponent implements OnInit{

    @ViewChild('formularioCategoria') formularioCategoria!: ElementRef;
    @ViewChild(MatPaginator) paginator!: MatPaginator;
    @ViewChild(MatSort) sort!: MatSort;

    categorias: Categoria[] = [];
    categoria: Categoria = { } as Categoria;
    editar: boolean = false;
    idEditar: number | null = null;

    dataSource!: MatTableDataSource<Categoria>;
    mostrarColumnas: String[ ] = ['idCategoria', 'categorias', 'descripcion', 'acciones'];

    constructor(private categoriaService: CategoriaService){   }

    ngOnInit(): void {
    this.findAll();
    }

    findAll(): void {
        this.categoriaService.findAll().subscribe(data => {
        //this.clientes = data;
        this.dataSource = new MatTableDataSource(data);
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
        });
    }

    save(): void{
        this.categoriaService.save(this.categoria).subscribe(() =>{
          this.categoria = { } as Categoria;
          this.findAll();
        });
      }
    
      update():void{
        if(this.idEditar !== null){
          this.categoriaService.update(this.idEditar, this.categoria).subscribe(() =>{ 
            this.categoria = { } as Categoria;
            this.editar = false;
            this.idEditar = null;
            this.findAll();
          });
        }
    }

    delete(): void{
        //this.clienteService.delete(this.cliente.idCliente).subscribe(() => { });
        Swal.fire({
          title: '¿Desea eliminar el dato?',
          text: 'Esta acción no se puede deshacer',
          icon: 'warning',
          showCancelButton: true,
          confirmButtonText: 'Si, eliminar',
          cancelButtonText: 'Cancelar',
          confirmButtonColor: '#d33',
          cancelButtonColor: '#3085d6'
          }).then((result) => { 
            if(result.isConfirmed){
              this.categoriaService.delete(this.categoria.idCategoria).subscribe(() => {  
                this.findAll();
                this.categoria = { } as Categoria;
                Swal.fire('Eliminado', 'El cliente ha sido eliminado', 'success');
              });
              
            }else{
              this.categoria = { } as Categoria;
            }
          });
        }

        // interaccion en la pagina web

  editarCategoria(categoria: Categoria): void{
    this.categoria= {...categoria};
    this.idEditar = categoria.idCategoria;
    this.editar = true;

    setTimeout(()  => { 
      this.formularioCategoria.nativeElement.scrollIntoView({behavior: 'smoot', block: 'start'});
     },100);
    }

  editarCategoriaCancelar(from: NgForm): void{
    this.categoria = { } as Categoria;
    this.idEditar = null;
    this.editar = false;
    from.resetForm();
  }

  guardar(form:NgForm): void{
    if(this.editar && this.idEditar!==null){
      this.update();
      form.resetForm();
    }else{
      this.save();
      form.resetForm();
    }
  }

  applyFilter(event: Event){
    const filtro = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filtro.trim().toLocaleLowerCase();
  }

}

