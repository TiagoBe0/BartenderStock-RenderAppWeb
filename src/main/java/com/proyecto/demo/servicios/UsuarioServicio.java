package com.proyecto.demo.servicios;

import com.proyecto.demo.entidades.Barra;
import com.proyecto.demo.entidades.Cristaleria;
import com.proyecto.demo.entidades.Foto;
import com.proyecto.demo.entidades.Proveedor;
import com.proyecto.demo.entidades.Ruptura;
import com.proyecto.demo.entidades.Usuario;
import com.proyecto.demo.entidades.Zona;
import com.proyecto.demo.enums.Rol;
import com.proyecto.demo.errores.ErrorServicio;
import com.proyecto.demo.repositorios.BarraRepositorio;
import com.proyecto.demo.repositorios.ProveedorRepositorio;
import com.proyecto.demo.repositorios.UsuarioRepositorio;
import com.proyecto.demo.repositorios.ZonaRepositorio;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UsuarioServicio implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    
    
    @Autowired
    private ProveedorServicio proveedorServicio;
    @Autowired
    private CristaleriaServicio cristaleriaServicio;

    @Autowired
    private FotoServicio fotoServicio;
    @Autowired
    private BarraServicio barraServicio;

     @Autowired
    private BarraRepositorio barraRepositorio;
     @Autowired
    private ProveedorRepositorio proveedorRepositorio;

@Transactional
public Barra registrarBarra(String idUsuario, String nombreBarra) throws ErrorServicio { // Nombre más claro

    // 1. Obtener y validar el Usuario
    Usuario usuario = usuarioRepositorio.findById(idUsuario)
            .orElseThrow(() -> new ErrorServicio("No se encontró el usuario solicitado con ID: " + idUsuario));

    // 2. Crear y configurar la nueva Barra
    Barra nuevaBarra = new Barra();
    nuevaBarra.setNombre(nombreBarra);
    nuevaBarra.setUsuario(usuario); // Asociar con el Usuario persistente

    // 3. Guardar la nueva Barra (se vuelve persistente y obtiene un ID)
    Barra barraGuardada = barraRepositorio.save(nuevaBarra);

    // 4. Actualizar la colección en el lado del Usuario (si la relación es bidireccional)
    // Asumiendo que Usuario tiene un getListaDeBarras() y setListaDeBarras()
    // y que la relación está bien mapeada (ej. @OneToMany(mappedBy="usuario", cascade={CascadeType.PERSIST, CascadeType.MERGE}))
    // Es común tener un método helper en Usuario como usuario.addBarra(barraGuardada);
    List<Barra> listaDeBarrasDelUsuario = usuario.getBarras(); // O como se llame tu getter
    if (listaDeBarrasDelUsuario == null) {
        listaDeBarrasDelUsuario = new ArrayList<>();
        usuario.setBarras(listaDeBarrasDelUsuario); // O como se llame tu setter
    }
    listaDeBarrasDelUsuario.add(barraGuardada);

    // 5. Actualizar otros campos del Usuario si es necesario
    if (usuario.getTodasLasCristalerias() != null) {
        usuario.setCapitalTotal(barraServicio.calcularPrecioTotal(usuario.getTodasLasCristalerias()));
    } else {
        usuario.setCapitalTotal(0f);
    }

    // 6. Guardar el Usuario para persistir los cambios en su lista de barras y capitalTotal
    usuarioRepositorio.save(usuario);

    return barraGuardada; // Devolver la entidad creada puede ser útil
}
    @Transactional
    public void registrar(MultipartFile archivo, String nombre, String apellido, String mail, String clave, String clave2) throws ErrorServicio {
        
        validar(nombre, apellido, mail, clave, clave2);

        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setMail(mail);
        usuario.setCapitalTotal(0);
        
        
        usuario.setRol(Rol.USUARIO);

        String encriptada = new BCryptPasswordEncoder().encode(clave);
        usuario.setClave(encriptada);
        
        //usuario.setAlta(new Date());

        Foto foto = fotoServicio.guardar(archivo);
        usuario.setFoto(foto);

        usuarioRepositorio.save(usuario);

    }
        @Transactional
    public void registrarAdmin(MultipartFile archivo, String nombre, String apellido, String mail, String clave, String clave2) throws ErrorServicio {
        System.out.println("LLEGARON DATOS A LOS SERVICIOOOOOOOOOOOOOOS");
        System.out.println("LLEGARON DATOS A LOS SERVICIOOOOOOOOOOOOOOS");

        validar(nombre, apellido, mail, clave, clave2);

        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setMail(mail);
        
        usuario.setRol(Rol.ADMIN);

        String encriptada = new BCryptPasswordEncoder().encode(clave);
        usuario.setClave(encriptada);
        
        //usuario.setAlta(new Date());

        Foto foto = fotoServicio.guardar(archivo);
        usuario.setFoto(foto);

        usuarioRepositorio.save(usuario);

    }

    @Transactional
    public void modificar(MultipartFile archivo, String id, String nombre, String apellido, String mail, String clave, String clave2) throws ErrorServicio {

        
        
        validar(nombre, apellido, mail, clave, clave2);

        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        if (respuesta.isPresent()) {

            Usuario usuario = respuesta.get();
            usuario.setApellido(apellido);
            usuario.setNombre(nombre);
            usuario.setMail(mail);
            
            String encriptada = new BCryptPasswordEncoder().encode(clave);
            usuario.setClave(encriptada);

            String idFoto = null;
            if (usuario.getFoto() != null) {
                idFoto = usuario.getFoto().getId();
            }

            Foto foto = fotoServicio.actualizar(idFoto, archivo);
            usuario.setFoto(foto);

            usuarioRepositorio.save(usuario);
        } else {

            throw new ErrorServicio("No se encontró el usuario solicitado");
        }

    }
    @Transactional
    public void modificarBarra( String id, String nombre) throws ErrorServicio {

        
        Barra barra = new Barra();
        barra.setNombre(nombre);
        barra.setUsuario(buscarPorId(id));
       
         barraRepositorio.save(barra);
        

        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        if (respuesta.isPresent()) {

            Usuario usuario = respuesta.get();
           
            actualizarListBarras(id, barra.getId());
            
            usuario.setCapitalTotal(barraServicio.calcularPrecioTotal(usuario.getTodasLasCristalerias()));
            
           
            

            

            usuarioRepositorio.save(usuario);
        } else {

            throw new ErrorServicio("No se encontró el usuario solicitado");
        }

    }
         @Transactional
    public void modificarProveedor( String id, String nombre,long contacto,String link) throws ErrorServicio {

        
        Proveedor barra = new Proveedor();
        barra.setNombre(nombre.toUpperCase());
        Usuario usuario = buscarPorId(id);
        barra.setUsuario(usuario);
        barra.setLink(link);
        barra.setCelular(contacto);
       
        usuarioRepositorio.save(usuario);
         proveedorRepositorio.save(barra);
        

        if (usuario!=null) {

          
            actualizarListProveedores(id, barra.getId());
            

            usuarioRepositorio.save(usuario);
        } else {

            throw new ErrorServicio("No se encontró el usuario solicitado");
        }

    }
    
    @Transactional
    public void actualizarListBarras(String idUsuario,String idBarra) throws ErrorServicio{
        //buscamos el usuario y getiamos las listas de Barra
    Usuario usuario = buscarPorId(idUsuario);
    List<Barra> barras = usuario.getBarras();
    //sumamos las barras a la list
    barras.add(barraServicio.buscarPorId(idBarra));
    usuario.setBarras(barras);
    int[] array= barraServicio.actualizarStockBarra(idUsuario);
    usuario.setTotalCristalerias(array[0]);
     usuario.setTotalInsumos(array[1]);
    usuario.setTotalDeBarras(barras.size());
    float suma=0.f;
    float sumaInsumos=0.f;
        for (Barra barra : barras) {
            if(!barra.isInsumo()){
            suma=suma+barra.getPrecioTotal();
            }else{
                
                 sumaInsumos=sumaInsumos+barra.getPrecioTotal();
            
            }
        }
        usuario.setCapitalTotal(suma);
        usuario.setCapitalTotalInsumos(sumaInsumos);
    
    
        usuarioRepositorio.save(usuario);
    
    }
    //RUPTURA DEL MES
    @Transactional
    public float actualizacionCosteMensualRupturas(String idUsuario,Calendar calendario) throws ErrorServicio{
        float costeMensual=0.f;
        Usuario usuario =buscarPorId(idUsuario);
        int diasLimpios=32;
        if(usuario!=null){
            for (Ruptura ruptura : usuario.getTodasLasRupturas()) {
                
                if(ruptura.getCalendario().get(Calendar.MONTH)==calendario.get(Calendar.MONTH)){
                    
                    costeMensual=costeMensual+ruptura.getCostoRuptura();
                    if(calendario.get(Calendar.DATE)-ruptura.getDia()<=diasLimpios){
                    
                    diasLimpios=calendario.get(Calendar.DATE)-ruptura.getDia();
                    }
                
                
                
                }
                
                
            }
            usuario.setDiasLimpios(diasLimpios);
            usuario.setCosteMensual(costeMensual);
            return costeMensual;
        }
        
        usuarioRepositorio.save(usuario);
    
    return costeMensual;
    
    }

@Transactional
public void actualizarListaDeCristalerias(Usuario usuario,List<Cristaleria> cristalerias){
    
    usuario.setTodasLasCristalerias(cristalerias);
    

        usuarioRepositorio.save(usuario);


}



    //CALCULAR CAPITAL TOTAL EN STCOK
@Transactional
    public void actualizarCapitalTotal(String idUsuario) throws ErrorServicio{
        actualizarNumeroTotalDeCristalerias(idUsuario);
        Usuario usuario = buscarPorId(idUsuario);
        
        if(usuario!=null){
            float suma=0.f;
            float sumaInsumos=0.f;
            List<Barra> barras = usuario.getBarras();
            for (Barra barra : barras) {
                if(barra.isInsumo()){
                    sumaInsumos=sumaInsumos+barra.getPrecioTotal();
                
                }
                else{
                    suma=suma+barra.getPrecioTotal();
                
                }
                
                
                
            }
        usuario.setCapitalTotal(suma);
        usuario.setCapitalTotalInsumos(sumaInsumos);
        int[] array = barraServicio.actualizarStockBarra(idUsuario);
        usuario.setTotalCristalerias(array[0]);
        usuario.setTotalInsumos(array[1]);
        Calendar calendario = new GregorianCalendar();
       

         usuario.setCosteMensualInsumos(actualizacionCosteMensualVencimientos(idUsuario, calendario));
            usuario.setCosteMensual(actualizacionCosteMensualRupturas(idUsuario, calendario));
        }
        
        usuarioRepositorio.save(usuario);
           
       
        }
    
    
    
    //VENCIMIENTOS
    @Transactional
    public float actualizacionCosteMensualVencimientos(String idUsuario,Calendar calendario) throws ErrorServicio{
        float costeMensual=0.f;
        Usuario usuario =buscarPorId(idUsuario);
        int diasLimpios=32;
        if(usuario!=null){
            for (Ruptura ruptura : usuario.getTodasLasRupturas()) {
                if(ruptura.isInsumo()){
                if(ruptura.getCalendario().get(Calendar.MONTH)==calendario.get(Calendar.MONTH)){
                    
                    costeMensual=costeMensual+ruptura.getCostoRuptura();
                    if(calendario.get(Calendar.DATE)-ruptura.getDia()<=diasLimpios){
                    
                    diasLimpios=calendario.get(Calendar.DATE)-ruptura.getDia();
                    }
                
                }
                
                }
                
                
            }
            usuario.setDiasLimpiosInsumos(diasLimpios);
            usuario.setCosteMensualInsumos(costeMensual);
            return costeMensual;
        }
    
        usuarioRepositorio.save(usuario);
    return costeMensual;
    
    }
@Transactional
public void actualizarCristaleriasAlterada(String idUsuario,String idCristaleria) throws ErrorServicio{
    
    Usuario usuario =buscarPorId(idUsuario);
    
    if(usuario!=null){
       
        
        for (Cristaleria cristaleria :  usuario.getTodasLasCristalerias()) {
            if(cristaleria.getId().equals(idCristaleria)){
            
                 usuario.getTodasLasCristalerias().remove(cristaleria);
                 usuario.getTodasLasCristalerias().add(cristaleriaServicio.buscarPorId(idCristaleria));
            }
            
        }
    
        
    }
    


        usuarioRepositorio.save(usuario);

}
    @Transactional
public void actualizarNumeroTotalDeCristalerias(String id ) throws ErrorServicio{

    
    Usuario usuario = buscarPorId(id);
    if(usuario!=null){
    
        List<Barra> barras = usuario.getBarras();
        int total=0;
        for (Barra barra : barras) {
            total=total+barra.getTotalUnidades();
        }
        
        usuario.setTotalCristalerias(total);
    }

        usuarioRepositorio.save(usuario);

}
    
@Transactional
    public void modificarCristaleria( String id, String nombre,String tipo, String descripcion, float precio, int enStock,String idBarra) throws ErrorServicio {

        
        Barra barra = new Barra();
        barra.setNombre(nombre);
        barra.setUsuario(buscarPorId(id));
       
         barraRepositorio.save(barra);
        

        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        if (respuesta.isPresent()) {

            Usuario usuario = respuesta.get();
           
            actualizarListBarras(id, barra.getId());
            
            
           
            

            

            usuarioRepositorio.save(usuario);
        } else {

            throw new ErrorServicio("No se encontró el usuario solicitado");
        }

    }
    @Transactional
    public void deshabilitar(String id) throws ErrorServicio {

        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        if (respuesta.isPresent()) {

            Usuario usuario = respuesta.get();
            usuario.setBaja(new Date());
            usuarioRepositorio.save(usuario);
        } else {

            throw new ErrorServicio("No se encontró el usuario solicitado");
        }

    }

    @Transactional
    public void habilitar(String id) throws ErrorServicio {

        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        if (respuesta.isPresent()) {

            Usuario usuario = respuesta.get();
            usuario.setBaja(null);
            usuarioRepositorio.save(usuario);
        } else {

            throw new ErrorServicio("No se encontró el usuario solicitado");
        }

    }
    
    @Transactional
    public void cambiarRol(String id) throws ErrorServicio{
    
    	Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
    	
    	if(respuesta.isPresent()) {
    		
    		Usuario usuario = respuesta.get();
    		
    		if(usuario.getRol().equals(Rol.USUARIO)) {
    			
    		usuario.setRol(Rol.ADMIN);
    		
    		}else if(usuario.getRol().equals(Rol.ADMIN)) {
    			usuario.setRol(Rol.USUARIO);
    		}
    	}
    }

    public void validar(String nombre, String apellido, String mail, String clave, String clave2) throws ErrorServicio {

        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorServicio("El nombre del usuario no puede ser nulo");
        }

        if (apellido == null || apellido.isEmpty()) {
            throw new ErrorServicio("El apellido del usuario no puede ser nulo");
        }

        if (mail == null || mail.isEmpty()) {
            throw new ErrorServicio("El mail no puede ser nulo");
        }

        if (clave == null || clave.isEmpty() || clave.length() <= 6) {
            throw new ErrorServicio("La clave del usuario no puede ser nula y tiene que tener mas de seis digitos");
        }
        if (!clave.equals(clave2)) {
            throw new ErrorServicio("Las claves deben ser iguales");
        }

       

    }
    
    
    //listar toda crtistaleria de barras
    public List<Cristaleria> listarTodaLaCristaleria(String id) throws ErrorServicio{
          
        Usuario usuario = buscarPorId(id);
        List<Barra> barras =usuario.getBarras();
        List<Cristaleria> todasLasCristalerias = null;
        
        for (Barra barra : barras) {
            
            for (Cristaleria obj : barra.getListaCristalerias()) {
                todasLasCristalerias.add(obj);
            }
            
        }
    return todasLasCristalerias;
    
    }
    
   //huscar cristaleria por id
    public List<Cristaleria> buscarCristaleriaPorIdUsuario(String id){
    
    return cristaleriaServicio.listarPorIdUsuario(id);
    }

    @Override
 @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
    	
        Usuario usuario = usuarioRepositorio.buscarPorMail(mail);
        
        if (usuario != null) {
        	
            List<GrantedAuthority> permisos = new ArrayList<>();
                        
            //Creo una lista de permisos! 
            GrantedAuthority p1 = new SimpleGrantedAuthority("ROLE_"+ usuario.getRol());
            
            permisos.add(p1);
         
            //Esto me permite guardar el OBJETO USUARIO LOG, para luego ser utilizado
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession(true);
            
            session.setAttribute("usuariosession", usuario); // llave + valor

            User user = new User(usuario.getMail(), usuario.getClave(), permisos);
            
    
            
            return user;

        } else {
            return null;
        }

    }

 @Transactional(readOnly=true)
    public Usuario buscarPorId(String id) throws ErrorServicio {

        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        if (respuesta.isPresent()) {

            Usuario usuario = respuesta.get();
            return usuario;
        } else {

            throw new ErrorServicio("No se encontró el usuario solicitado");
        }

    }
    
   @Transactional(readOnly=true)
    public List<Usuario>  todosLosUsuarios(){
 
        return usuarioRepositorio.findAll();
        
    }
    @Transactional(readOnly=true)
    public List<Barra>  todasLasBarras(String idUsuario) throws ErrorServicio{
     Usuario usuario =buscarPorId(idUsuario);
     
 
        return usuario.getBarras();
        
    }
    
   /*
    public void actualizarListBarras(String idUsuario,String idBarra) throws ErrorServicio{
        //buscamos el usuario y getiamos las listas de Barra
    Usuario usuario = buscarPorId(idUsuario);
    List<Barra> barras = usuario.getBarras();
    //sumamos las barras a la list
    barras.add(barraServicio.buscarPorId(idBarra));
    usuario.setBarras(barras);
    int[] array= barraServicio.actualizarStockBarra(idUsuario);
    usuario.setTotalCristalerias(array[0]);
     usuario.setTotalInsumos(array[1]);
    usuario.setTotalDeBarras(barras.size());
    float suma=0.f;
    float sumaInsumos=0.f;
        for (Barra barra : barras) {
            if(!barra.isInsumo()){
            suma=suma+barra.getPrecioTotal();
            }else{
                
                 sumaInsumos=sumaInsumos+barra.getPrecioTotal();
            
            }
        }
        usuario.setCapitalTotal(suma);
        usuario.setCapitalTotalInsumos(sumaInsumos);
    
    
    
    }
   */
    @Transactional
 public void actualizarListProveedores(String idUsuario,String idBarra) throws ErrorServicio{
        //buscamos el usuario y getiamos las listas de Barra
    Usuario usuario = buscarPorId(idUsuario);
    List<Proveedor> barras = usuario.getProveedores();
    //sumamos las barras a la list
    barras.add(proveedorServicio.buscarPorId(idBarra));
    usuario.setProveedores(barras);
    
        usuarioRepositorio.save(usuario);
    
    
    
    }

    
  
}
