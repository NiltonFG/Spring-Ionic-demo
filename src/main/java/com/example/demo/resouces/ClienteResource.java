package com.example.demo.resouces;

import com.example.demo.domain.Cliente;
import com.example.demo.domain.Cliente;
import com.example.demo.domain.dto.ClienteDTO;
import com.example.demo.domain.dto.ClienteNewDTO;
import com.example.demo.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/clientes")
public class ClienteResource {

    @Autowired
    private ClienteService service;

    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public ResponseEntity<?> find(@PathVariable Integer id) {
        Cliente obj = service.find(id);
        return ResponseEntity.ok().body(obj);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> insert(@Valid @RequestBody ClienteNewDTO objDto){
        Cliente obj = service.fromDTO(objDto);
        //obj = service.insert(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @RequestMapping(value = "/{id}",method = RequestMethod.PUT)
    public ResponseEntity<?> update(@Valid @RequestBody ClienteDTO objDTO, @PathVariable Integer id){
        objDTO.setId(id);
        Cliente obj = service.fromDTO(objDTO);
        service.update(obj);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<ClienteDTO>> findAll(){
        List<Cliente> list = service.findAll() ;
        List<ClienteDTO> dtoList = list.stream().map(obj -> new ClienteDTO(obj)).collect(Collectors.toList());
        return ResponseEntity.ok().body(dtoList);
    }

    @RequestMapping(value = "/page",method = RequestMethod.GET)
    public ResponseEntity<Page<ClienteDTO>> findPage(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "lines", defaultValue = "24") Integer linesPerPage,
            @RequestParam(value = "order", defaultValue = "nome") String orderBy,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction){
        Page<Cliente> list = service.findPage(page,linesPerPage,orderBy,direction);
        Page<ClienteDTO> dtoList = list.map(obj -> new ClienteDTO(obj));
        return ResponseEntity.ok().body(dtoList);
    }
}
