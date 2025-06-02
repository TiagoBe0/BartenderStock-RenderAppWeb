

package com.proyecto.demo.servicios;

import com.proyecto.demo.entidades.Barra;
import com.proyecto.demo.entidades.Cristal;
import com.proyecto.demo.entidades.Cristaleria;
import com.proyecto.demo.entidades.Foto;
import com.proyecto.demo.entidades.Usuario;
import com.proyecto.demo.errores.ErrorServicio;
import com.proyecto.demo.repositorios.BarraRepositorio;
import com.proyecto.demo.repositorios.CristalRepositorio;

import com.proyecto.demo.repositorios.CristaleriaRepositorio;
import com.proyecto.demo.repositorios.UsuarioRepositorio;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CristaleriaServicio {
    
    
    @Autowired
    private CristaleriaRepositorio cristaleriaRepositorio;
     @Autowired
    private BarraServicio barraServicio;
     @Autowired
     private CristalServicio cristalServicio;
     
     @Autowired
     private UsuarioServicio usuarioServicio;
       @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    
     
     @Autowired
     private BarraRepositorio barraRepositorio;
      @Autowired
     private CristalRepositorio cristalRepositorio;
    
     @Autowired
    private FotoServicio fotoServicio;

    
    @Transactional
    public void registrar(MultipartFile archivo, String tipo, String descripcion, float precio, int enStock,String idBarra) throws ErrorServicio {


       Cristaleria cristaleria = new Cristaleria();
       
      
        Foto foto = fotoServicio.guardar(archivo);
        cristaleria.setFoto(foto);
        cristaleria.setAlta(new Date());
        cristaleria.setDescripcion(descripcion);
        cristaleria.setPrecio(precio);
        cristaleria.setEnStock(enStock);
        cristaleria.setTipo(tipo);
        Calendar calendario = new GregorianCalendar();
        cristaleria.setCalendario(calendario);
        //Creamos una barra de pertenecencia y añadimos a lista de cristaleria comoa tributo
        Barra barra =barraServicio.buscarPorId(idBarra);
        Usuario usuario = usuarioServicio.buscarPorId(barra.getUsuario().getId());
        cristaleria.setIdUsuario(barra.getUsuario().getId());
         List<Cristaleria> cristalerias = barra.getListaCristalerias();
         cristalerias.add(cristaleria);
       
         barra.setListaCristalerias(cristalerias);
          float suma = barraServicio.calcularPrecioTotal(cristalerias);
          usuario.setCapitalTotal(suma);
        barra.setPrecioTotal(suma);
         
        cristaleria.setBarraPerteneciente(barra);
        
        usuarioRepositorio.save(usuario);
        barraRepositorio.save(barra);
        cristaleriaRepositorio.save(cristaleria);
        

       

    }
    
    public List<Cristaleria> listarCristaleriasPorIdUsuario(String idUsuario){
    
    
   return cristaleriaRepositorio.buscarPorIdUsuario(idUsuario);
   
    
    }
    
    
    
     public List<Cristaleria> listarInsumosPorIdUsuario(String idUsuario){
    
    
    return cristaleriaRepositorio.buscarPorIdUsuario(idUsuario);
  
       
    
    }
    
 
    public List<Cristaleria> listarPorIdUsuario(String idUsuario){
    
    
    return cristaleriaRepositorio.buscarPorIdUsuario(idUsuario);
    }
     

@Transactional
public void modificar(MultipartFile archivo, String tipo, String descripcion, float precio, int enStock, String idBarra, String id, String idCristal) throws ErrorServicio {
    Cristal cristal = null;
    // Cristal cristalNuevo = null; // Esta variable no se usa en el código que me mostraste
    Cristaleria cristaleria = new Cristaleria(); // (1) Creas una NUEVA instancia de Cristaleria. Es TRANSIENT.

    
        // ¿Qué pasa si idCristal se provee pero cristalServicio.buscarPorId(idCristal) devuelve null?
        // La lógica actual continuaría y podría intentar crear una nueva foto más abajo.
  
            cristaleria.setDescripcion(idCristal);

    if (idBarra != null && !idBarra.isEmpty()) { // Chequear también !idBarra.isEmpty()
        Barra barraPerteneciente = barraServicio.buscarPorId(idBarra);
        if (barraPerteneciente == null) { // Validar que la barra exista
            throw new ErrorServicio("La barra con ID " + idBarra + " no fue encontrada.");
        }

        Usuario usuario = usuarioServicio.buscarPorId(id);
        if (usuario == null) { // Validar que el usuario exista
            throw new ErrorServicio("El usuario con ID " + id + " no fue encontrado.");
        }

        // Lógica de insumo y registro de 'Cristal' independiente:
        // Estas llamadas a cristalServicio.registrar(archivo, null, X) crean y guardan entidades 'Cristal'.
        // Sin embargo, el 'Cristal' resultante de estas llamadas NO se está asociando explícitamente
        // con el objeto 'cristaleria' que estás construyendo en este método mediante 'cristaleria.setCristalRepo()'.
        // Si la intención es que este 'Cristal' recién registrado sea el 'CristalRepo' de 'cristaleria',
        // necesitarías capturar el resultado de 'cristalServicio.registrar' y asignarlo.
        if (barraPerteneciente.isInsumo()) {
            cristaleria.setInsumo(true);
            if (idCristal == null && archivo != null && !archivo.isEmpty()) {
                // Supongamos que cristalServicio.registrar devuelve el Cristal guardado:
                // Cristal nuevoCristalRegistrado = cristalServicio.registrar(archivo, null, 1);
                // cristaleria.setCristalRepo(nuevoCristalRegistrado); // ¿Deberías hacer esto?
            }
        } else {
            cristaleria.setInsumo(false);
            if (idCristal == null && archivo != null && !archivo.isEmpty()) {
                // Cristal nuevoCristalRegistrado = cristalServicio.registrar(archivo, null, -1);
                // cristaleria.setCristalRepo(nuevoCristalRegistrado); // ¿O esto?
            }
        }

        cristaleria.setPrecio(precio);
        cristaleria.setEnStock(enStock);
        cristaleria.setPrecioTotal(); // Asegúrate que este método no dependa de un ID generado por la BD si se llama antes de guardar.
        cristaleria.setIdUsuario(id); // (6) Guardas el ID del usuario como String. ¿No sería mejor una relación @ManyToOne Usuario usuario;?
        cristaleria.setTipo(tipo);

        // (7) Estableces la relación con 'Barra' (que es PERSISTENT).
        // Esto es clave: 'cristaleria' (TRANSIENT) ahora apunta a 'barraPerteneciente' (PERSISTENT).
        cristaleria.setBarraPerteneciente(barraPerteneciente);
        cristaleria.setBarraPertenecienteNombre(barraPerteneciente.getNombre());

        // >>> PUNTO CRÍTICO PARA TRANSIENT OBJECT EXCEPTION <<<
        // Se añaden 'cristaleria' (TRANSIENT) a listas de entidades PERSISTENT ('barraPerteneciente', 'usuario')
        // y luego se guardan esas entidades PERSISTENT.

        // Para evitar el TransientObjectException, 'cristaleria' debe guardarse ANTES o las relaciones
        // en Barra y Usuario hacia Cristaleria deben tener configurado CascadeType.PERSIST o CascadeType.ALL.

        // ----- SOLUCIÓN PROPUESTA: Guardar 'cristaleria' aquí -----
        // Antes de añadirla a las listas de barraPerteneciente o usuario, o antes de guardar barraPerteneciente o usuario.
        // Asumiendo que Foto y Cristal (CristalRepo) ya son persistentes o se manejan con cascada desde Cristaleria.
        Cristaleria cristaleriaGuardada = cristaleriaRepositorio.save(cristaleria); // (8) AHORA 'cristaleriaGuardada' es PERSISTENT.

        // Ahora usa 'cristaleriaGuardada' (la instancia persistente)
        List<Cristaleria> cristaleriasEnBarra = barraPerteneciente.getListaCristalerias();
        if (cristaleriasEnBarra == null) { // Siempre buena idea inicializar listas si pueden ser null
            cristaleriasEnBarra = new ArrayList<>();
        }
        cristaleriasEnBarra.add(cristaleriaGuardada); // Añades la instancia PERSISTENT
        barraPerteneciente.setListaCristalerias(cristaleriasEnBarra); // Actualizas la lista en Barra
        barraPerteneciente.setTotalUnidades(barraPerteneciente.getTotalUnidades() + cristaleriaGuardada.getEnStock());

        List<Cristaleria> cristaleriaUsuario = usuario.getTodasLasCristalerias();
        if (cristaleriaUsuario == null) {
            cristaleriaUsuario = new ArrayList<>();
        }
        cristaleriaUsuario.add(cristaleriaGuardada); // Añades la instancia PERSISTENT
        usuario.setTodasLasCristalerias(cristaleriaUsuario); // Actualizas la lista en Usuario

        // Recalcular precios para barraPerteneciente
        if (barraPerteneciente.isInsumo()) {
            float suma = barraServicio.calcularPrecioTotalInsumos(cristaleriasEnBarra);
            barraPerteneciente.setPrecioTotal(suma);
        } else {
            float suma = barraServicio.calcularPrecioTotal(cristaleriasEnBarra);
            barraPerteneciente.setPrecioTotal(suma);
        }

        // (9) Guardas 'barraPerteneciente'. Ahora no debería haber problema porque 'cristaleriaGuardada' en su lista ya es persistente.
        barraRepositorio.save(barraPerteneciente);

        // (10) Guardas 'usuario'. Mismo razonamiento.
        usuarioRepositorio.save(usuario);

        // (11) El guardado explícito de 'cristaleria' ya se hizo en el paso (8).
        // Si tienes CascadeType.ALL o PERSIST desde Barra/Usuario a Cristaleria, el save de Barra/Usuario
        // podría haber guardado 'cristaleria' también, pero es más claro y seguro guardarla explícitamente
        // primero si es la entidad principal que estás creando y luego asociarla.
        // Si la guardaste en (8), este save es redundante y podría ser un update si el objeto es el mismo.

        /*
        // Esta lógica de actualizar foto sería más para un 'modificar' real de una Cristaleria existente
        String idFoto = null;
        if (cristaleriaGuardada.getFoto() != null && archivo != null && !archivo.isEmpty()) { // Asegúrate que archivo no sea nulo/vacío
            idFoto = cristaleriaGuardada.getFoto().getId();
            Foto fotoActualizada = fotoServicio.actualizar(idFoto, archivo); // 'actualizar' en lugar de 'guardar'
            cristaleriaGuardada.setFoto(fotoActualizada);
            cristaleriaRepositorio.save(cristaleriaGuardada); // Guardar de nuevo Cristaleria si su foto cambió
        } else if (cristaleriaGuardada.getFoto() == null && archivo != null && !archivo.isEmpty()) {
            // Caso en que la cristaleria no tenía foto y ahora se le añade una.
            Foto nuevaFoto = fotoServicio.guardar(archivo);
            cristaleriaGuardada.setFoto(nuevaFoto);
            cristaleriaRepositorio.save(cristaleriaGuardada);
        }
        */

    } else {
        // La validación original decía "No se encontró el usuario solicitado", pero el 'if' es sobre idBarra.
        throw new ErrorServicio("El ID de la Barra no fue proporcionado o es inválido.");
    }
}
    
     @Transactional
    public void borrarTodo(){
    
    cristaleriaRepositorio.deleteAll();
    }
       
      @Transactional
    public void alterar(MultipartFile archivo, String tipo, String descripcion, float precio, int enStock,String id,String idUsuario) throws ErrorServicio {
                
                
         if(!id.isEmpty()){
             Optional<Cristaleria> respuesta = cristaleriaRepositorio.findById(id);
             if(respuesta.isPresent()){
             
                 Cristaleria cristaleria =  respuesta.get();
                 
                 cristaleria.setDescripcion(descripcion);
                 cristaleria.setPrecio(precio);
                 cristaleria.setTipo(tipo);
                 cristaleria.setIdUsuario(idUsuario);
                 cristaleria.setEnStock(enStock);
                 cristaleria.setPrecioTotal();
                 if(archivo!=null){
                     
                     Foto foto =fotoServicio.guardar(archivo);
                     if(foto!=null){
                     
                         cristaleria.setFoto(foto);
                     }
                 
                 
                 }
                 cristaleriaRepositorio.save(cristaleria);
               
                 alterarActulizacion(idUsuario, id);
                
             }
          
       
         else {

            throw new ErrorServicio("No se encontró el usuario solicitado");
        }
         }

    }
    public List<Cristaleria> todasCristalrias(){
 
        return cristaleriaRepositorio.findAll();
        
    }
    
   @Transactional
     public void deshabilitar(String id) throws ErrorServicio{
          
         Cristaleria cristaleria= buscarPorId(id);
         if(cristaleria.isActivo()){
             cristaleria.setActivo(false);
         }
         else{
             cristaleria.setActivo(true);
         }
         cristaleriaRepositorio.save(cristaleria);
     }
     
     @Transactional
     public void alterarActulizacion(String idUsuario , String idCristaleria) throws ErrorServicio{
         
         Cristaleria cristaleria =buscarPorId(idCristaleria);
          Usuario usuario = usuarioServicio.buscarPorId(idUsuario);
                String idBarra = cristaleria.getBarraPerteneciente().getId();
                Barra barra = barraServicio.buscarPorId(idBarra);
                List<Cristaleria> cristalerias=barra.getListaCristalerias();
                List<Cristaleria> cristaleriasUsuario=usuario.getTodasLasCristalerias();
                
                 for (Cristaleria cristaleria1 : cristalerias) {
                     if(cristaleria1.getId().equals(idCristaleria)){
                         
                         cristaleria.setBarraPertenecienteNombre(barra.getNombre());
                         cristaleria1.setEnStock(cristaleria.getEnStock());
                         cristaleria1.setDescripcion(cristaleria.getDescripcion());
                         cristaleria1.setPrecio(cristaleria.getPrecio());
                         cristaleria1.setTipo(cristaleria.getTipo());
                         cristaleria1.setPrecioTotal();
                         
                         barra.setListaCristalerias(cristalerias);
                     
                     
                     }
                 }
                 int conteo=0;
                   for (Cristaleria cristaleria3 : cristaleriasUsuario) {
                     if(cristaleria3.getId().equals(idCristaleria)){
                         
                           cristaleria.setBarraPertenecienteNombre(barra.getNombre());
                         cristaleria3.setEnStock(cristaleria.getEnStock());
                         cristaleria3.setDescripcion(cristaleria.getDescripcion());
                         cristaleria3.setPrecio(cristaleria.getPrecio());
                         cristaleria3.setTipo(cristaleria.getTipo());
                         cristaleria3.setPrecioTotal();
                         usuario.setTodasLasCristalerias(cristaleriasUsuario);
                     
                     
                     }
                     conteo=conteo+cristaleria3.getEnStock();
                 }
                
                 
               if(barra.isInsumo()){
                   
                    float suma = barraServicio.calcularPrecioTotalInsumos(cristalerias);
                    barra.setPrecioTotal(suma);
               }
               else{
                    float suma = barraServicio.calcularPrecioTotal(cristalerias);
                    barra.setPrecioTotal(suma);
               
               }
               
                 
                 barra.setTotalUnidades(conteo);
                
                  //usuarioServicio.actualizarListBarras(idUsuario, idBarra);
                 //usuarioServicio.actualizarCapitalTotal(idUsuario);
                 usuarioServicio.actualizarNumeroTotalDeCristalerias(idUsuario);
         barraRepositorio.save(barra);
     
     
     
     }
    
    
    public Cristaleria buscarPorId(String id) throws ErrorServicio {

        Optional<Cristaleria> respuesta = cristaleriaRepositorio.findById(id);
        if (respuesta.isPresent()) {

            Cristaleria cristaleria = respuesta.get();
            return cristaleria;
        } else {

            throw new ErrorServicio("No se encontró la cristaleria solicitada");
        }

    }
    
}
