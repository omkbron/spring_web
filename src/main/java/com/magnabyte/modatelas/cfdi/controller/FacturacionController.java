package com.magnabyte.modatelas.cfdi.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.transform.stream.StreamSource;

import jcifs.Config;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.magnabyte.modatelas.cfdi.model.TicketXml;
import com.magnabyte.modatelas.cfdi.model.TransaccionHeaderXml;
import com.magnabyte.modatelas.cfdi.model.TransaccionXml;

@Controller
public class FacturacionController {

	private static final Logger log = LoggerFactory
			.getLogger(FacturacionController.class);

	@Autowired
	private Unmarshaller unmarshaller;

	@Autowired
	private Marshaller marshaller;
	
	@RequestMapping("/datosTicket")
	public String datosTicket(ModelMap model) throws IOException {
		log.debug("datos Ticket...");
		model.put("ticketHeader", new TransaccionHeaderXml());
//		SmbFileInputStream is = null;
//		try {
//			int suc = 999;
//			int caja = 98;
//			int ticket = 19;
//			String regex = suc + "_" + caja + "_" + ticket + "_\\d\\d\\d\\d\\d\\d\\d\\d\\d\\d\\d\\d\\d\\d\\.xml";
//			log.debug(regex);
//			Pattern pattern = Pattern.compile(regex);
//			Matcher matcher = pattern.matcher("999_98_19_20131101101115.xml");
//			log.debug("existe?" + matcher.matches());
//			log.debug("Recuperando el flujo del archivo");
//			Config.setProperty("jcifs.smb.client.useExtendedSecurity", "false");
//			is = new SmbFileInputStream("smb://10.2.200.149/users/equipoDesarrollo03/Documents/ovelasco/DocumentosIncidentes/999_98_19_20131101101115.xml");
////			is = new SmbFileInputStream("smb://10.2.200.129/cfdi/999_98_19_20131101101115.xml");
//			TicketXml ticketXml = (TicketXml) unmarshaller.unmarshal(new StreamSource(is));
//			log.debug("El ticket " + ticketXml);
//		} finally {
//			if (is != null) {
//				is.close();
//			}
//		}

		return "factura/datosTicket";
	}
	
	@RequestMapping(value="/validarTicket", method=RequestMethod.POST)
	public String validarTicket(@ModelAttribute("ticketHeader") TransaccionHeaderXml ticketHeader, ModelMap model) {
		TransaccionXml transaccion = new TransaccionXml();
		transaccion.setHeader(ticketHeader);
		TicketXml ticket = new TicketXml(transaccion);
		model.put("ticket", ticket);
		log.debug("ticket" + ticket);
		return "factura/resultadoTicket";
	}
	public static void main(String[] args) throws MalformedURLException, SmbException {
		Config.setProperty("jcifs.smb.client.useExtendedSecurity", "false");
		int suc = 999;
		int caja = 98;
		int ticket = 19;
		String regex = suc + "_" + caja + "_" + ticket + "_\\d\\d\\d\\d\\d\\d\\d\\d\\d\\d\\d\\d\\d\\d\\.xml";
//		String regex = suc + "_" + caja + "_" + ticket + "_\\d+.xml";
		log.debug(regex);
		Pattern pattern = Pattern.compile(regex);
		SmbFile dir = new SmbFile("smb://10.2.200.149/users/equipoDesarrollo03/Documents/ovelasco/DocumentosIncidentes/");
		if(dir.exists()) {
			SmbFile[] files = dir.listFiles();
			
			for (SmbFile file : files) {
				Matcher matcher = pattern.matcher(file.getName());
				log.debug(file.getName());
				if (matcher.matches()) {
					log.debug("se ha encontrado una coincidencia");
				}
				
			}
		}
	}
}
