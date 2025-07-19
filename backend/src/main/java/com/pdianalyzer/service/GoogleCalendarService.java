package com.pdianalyzer.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.*;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.http.HttpCredentialsAdapter;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

@Service
public class GoogleCalendarService {

  private Calendar calendarService;

  private static final String APPLICATION_NAME = "SmartHire PRO - PDAnalyzer";
  private static final String SERVICE_ACCOUNT_KEY_PATH = "C:\\json-calendar-service-key\\smarthire-pdi-calendar-51cc629762a8.json";
  private static final String CALENDAR_ID = "0c67d8bee0f4c8b8ff2437560f7b2f0988f18a15a2f045eeb2d88f2aae0684dc@group.calendar.google.com";

  @PostConstruct
  public void init() {
    try {
      // Carrega o arquivo de forma mais robusta
      InputStream in = GoogleCalendarService.class.getClassLoader()
        .getResourceAsStream("smarthire-pdi-calendar-51cc629762a8.json");

      if (in == null) {
        throw new FileNotFoundException("Arquivo de credenciais não encontrado");
      }

      GoogleCredentials credentials = GoogleCredentials.fromStream(in)
        .createScoped(Collections.singleton("https://www.googleapis.com/auth/calendar"));

      this.calendarService = new Calendar.Builder(
        GoogleNetHttpTransport.newTrustedTransport(),
        GsonFactory.getDefaultInstance(),
        new HttpCredentialsAdapter(credentials))
        .setApplicationName(APPLICATION_NAME)
        .build();
    } catch (GeneralSecurityException | IOException e) {
      throw new RuntimeException("Falha ao inicializar Google Calendar Service: " + e.getMessage(), e);
    }
  }

  public String agendarReuniaoComGoogleMeet(
    String titulo, String descricao, ZonedDateTime inicio, ZonedDateTime fim, List<String> convidadosEmails
  ) throws IOException {
    Event event = new Event()
      .setSummary(titulo)
      .setDescription(descricao);

    DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    EventDateTime start = new EventDateTime()
      .setDateTime(new com.google.api.client.util.DateTime(inicio.format(formatter)))
      .setTimeZone(inicio.getZone().toString());

    EventDateTime end = new EventDateTime()
      .setDateTime(new com.google.api.client.util.DateTime(fim.format(formatter)))
      .setTimeZone(fim.getZone().toString());

    event.setStart(start);
    event.setEnd(end);

    // entrando com o google meet
    ConferenceData conferenceData = new ConferenceData();
    CreateConferenceRequest createConferenceRequest = new CreateConferenceRequest();
    createConferenceRequest.setRequestId("PDI-fdbk-" + System.currentTimeMillis());
    conferenceData.setCreateRequest(createConferenceRequest);
    event.setConferenceData(conferenceData);

    Event createdEvent = calendarService.events().insert(
        CALENDAR_ID, event)
      .setConferenceDataVersion(1)
      .execute();

    Event fullEventDetails = calendarService.events().get(CALENDAR_ID, createdEvent.getId()).execute();

    return fullEventDetails.getHangoutLink();
  }
}

