package com.example.demo.config;

import com.example.demo.domain.*;
import com.example.demo.domain.enums.EstadoPagamento;
import com.example.demo.domain.enums.TipoCLiente;
import com.example.demo.repositories.*;
import com.example.demo.services.EmailService;
import com.example.demo.services.SmtpEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;

@Configuration
@Profile("dev")
public class devConfig {

    @Autowired
    private CategoriaRepository repository;

    @Autowired
    private ProdutoRepository repositoryP;

    @Autowired
    private EstadoRepository estadoRepository;

    @Autowired
    private CidadeRepository cidadeRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Autowired
    ItemPedidoRepository itemPedidoRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String strategy;

    @Bean
    public boolean intantiateDataBase() throws ParseException {

        if (!"create".equals(strategy)) {
            return false;
        }

        Categoria c1 = new Categoria(null, "Informatica");
        Categoria c2 = new Categoria(null, "Escritorio");
        Categoria c3 = new Categoria(null, "Cama mesa e banho");
        Categoria c4 = new Categoria(null, "Eletrônicos");
        Categoria c5 = new Categoria(null, "Jardinagem");
        Categoria c6 = new Categoria(null, "Decoração");
        Categoria c7 = new Categoria(null, "Perfumaria");

        Produto p1 = new Produto(null, "pc", 2000.00);
        Produto p2 = new Produto(null, "impressora", 500.00);
        Produto p3 = new Produto(null, "mouse", 100.00);

        c1.getProdutos().addAll(Arrays.asList(p1, p2, p3));
        c2.getProdutos().addAll(Arrays.asList(p2));

        p1.getCategorias().addAll(Arrays.asList(c1));
        p2.getCategorias().addAll(Arrays.asList(c1, c2));
        p3.getCategorias().addAll(Arrays.asList(c1));

        repository.saveAll(Arrays.asList(c1, c2, c3, c4, c5, c6, c7));
        repositoryP.saveAll(Arrays.asList(p1, p2, p3));

        Estado est1 = new Estado(null, "Minas Gerais");
        Estado est2 = new Estado(null, "São Paulo");

        Cidade cid1 = new Cidade(null, "Uberlândia", est1);
        Cidade cid2 = new Cidade(null, "São Paulo", est2);
        Cidade cid3 = new Cidade(null, "Campinas", est2);

        est1.getCidades().addAll(Arrays.asList(cid1));
        est2.getCidades().addAll(Arrays.asList(cid2, cid3));

        estadoRepository.saveAll(Arrays.asList(est1, est2));
        cidadeRepository.saveAll(Arrays.asList(cid1, cid2, cid3));

        Cliente cli1 = new Cliente(null, "Maria Silva", "maria@gmail.com", "11122121222", TipoCLiente.PESSOAFISICA, null);
        cli1.getTelefones().addAll(Arrays.asList("555979797", "1542365478"));

        Endereco e1 = new Endereco(null, "Rua Flores", "300", "Apto 231", "Jardim", "4444444", cid2, cli1);
        Endereco e2 = new Endereco(null, "Rua Torres", "20", "Apt 4", "Mogi", "44448888", cid1, cli1);

        cli1.getEnderecos().addAll(Arrays.asList(e1, e2));

        clienteRepository.saveAll(Arrays.asList(cli1));
        enderecoRepository.saveAll(Arrays.asList(e1, e2));

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Pedido ped1 = null;
        try {
            ped1 = new Pedido(null, sdf.parse("30/09/2020 10:32"), cli1, e1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Pedido ped2 = new Pedido(null, sdf.parse("20/10/2020 10:00"), cli1, e2);

        PagamentoCartao pagto1 = new PagamentoCartao(null, EstadoPagamento.QUITADO, ped1, 6);
        ped1.setPagamento(pagto1);

        PagamentoBoleto pagto2 = new PagamentoBoleto(null, EstadoPagamento.PENDENTE, ped2, sdf.parse("20/07/2020 10:00"), null);
        ped2.setPagamento(pagto2);

        cli1.getPedidos().addAll(Arrays.asList(ped1, ped2));

        pedidoRepository.saveAll(Arrays.asList(ped1, ped2));
        pagamentoRepository.saveAll(Arrays.asList(pagto1, pagto2));

        ItemPedido ip1 = new ItemPedido(ped1, p1, 0.00, 1, 2000.00);
        ItemPedido ip2 = new ItemPedido(ped1, p3, 0.00, 2, 80.00);
        ItemPedido ip3 = new ItemPedido(ped2, p2, 100.00, 1, 800.00);

        ped1.getItens().addAll(Arrays.asList(ip1, ip2));
        ped2.getItens().add(ip3);

        p1.getItens().addAll(Arrays.asList(ip1));
        p2.getItens().addAll(Arrays.asList(ip3));
        p3.getItens().addAll(Arrays.asList(ip2));

        itemPedidoRepository.saveAll(Arrays.asList(ip1, ip2, ip3));
        return true;
    }

    @Bean
    public EmailService emailService() {
        return new SmtpEmailService();
    }

}
