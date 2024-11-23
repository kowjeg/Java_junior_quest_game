package ru.saveldu.java_junior_quest_game;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class QuestServletTest {

    @Mock
    HttpSession session;

    @Mock
    HttpServletRequest request;

    @Mock
    HttpServletResponse response;

    @Mock
    StepData dataPrevStep;
    @Mock
    StepData dataStep;

    @Mock
    RequestDispatcher dispatcher;

    @InjectMocks
    QuestServlet questServlet;

    @BeforeEach
    void setUp() {
        session = mock(HttpSession.class);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        dataPrevStep = mock(StepData.class);
        dataStep = mock(StepData.class);
        dispatcher = mock(RequestDispatcher.class);
        questServlet = new QuestServlet();

    }
    @Test
    void testRightAnswerAndGoToNextStep() throws ServletException, IOException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        // Мокаем параметры шага
        when(request.getSession()).thenReturn(Mockito.mock(HttpSession.class));
        when(request.getSession().getAttribute("step")).thenReturn(1);
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        when(request.getParameter("answer")).thenReturn("2");
        when(dataPrevStep.getRightChoice()).thenReturn(2);

        // Вызываем метод
        Method method = QuestServlet.class.getDeclaredMethod("processStep", HttpServletRequest.class, HttpServletResponse.class, int.class, StepData.class, StepData.class);
        method.setAccessible(true); // Даем доступ к приватному методу


        method.invoke(questServlet, request, response, 1, dataStep, dataPrevStep);

        // Проверяем, что правильный метод был вызван для первого шага
        verify(request).getRequestDispatcher("quest.jsp");
    }
    @Test
    void testWrongAnswerAndGoToNextStep() throws ServletException, IOException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        // Мокаем необходимые объекты


        // Мокаем параметры шага
        when(request.getSession()).thenReturn(Mockito.mock(HttpSession.class));
        when(request.getSession().getAttribute("step")).thenReturn(1);
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        when(request.getParameter("answer")).thenReturn("1");
        when(dataPrevStep.getRightChoice()).thenReturn(3);

        // нашли метод через рефлексию
        Method method = QuestServlet.class.getDeclaredMethod("processStep", HttpServletRequest.class, HttpServletResponse.class, int.class, StepData.class, StepData.class);
        method.setAccessible(true); // Даем доступ к приватному методу

        method.invoke(questServlet, request, response, 1, dataStep, dataPrevStep);

        // при неправильном ответе должен быть редирект в gameover
        verify(request).getRequestDispatcher("gameover.jsp");
    }

    @Test
    void testLastRightAnswerAndVictory() throws ServletException, IOException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        // Мокаем параметры шага
        when(request.getSession()).thenReturn(Mockito.mock(HttpSession.class));
        when(request.getSession().getAttribute("step")).thenReturn(7);
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        when(request.getParameter("answer")).thenReturn("2");
        when(dataPrevStep.getRightChoice()).thenReturn(2);

        // ищем приватный метод
        Method method = QuestServlet.class.getDeclaredMethod("processStep", HttpServletRequest.class, HttpServletResponse.class, int.class, StepData.class, StepData.class);
        method.setAccessible(true); // Даем доступ к приватному методу

        // Вызываем приватный метод через рефлексию
        method.invoke(questServlet, request, response, 7, dataStep, dataPrevStep);

        // Проверяем, что с правильным ответом на последнем шаге идет редирект на victory
        verify(request).getRequestDispatcher("victory.jsp");
    }
    @Test
    void testSessionInvalidatedOnGameOver() throws ServletException, IOException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        when(request.getSession()).thenReturn(session);
        when(request.getSession().getAttribute("step")).thenReturn(1);
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        when(request.getParameter("answer")).thenReturn("1"); // Неправильный ответ
        when(dataPrevStep.getRightChoice()).thenReturn(2);


        Method method = QuestServlet.class.getDeclaredMethod("processStep", HttpServletRequest.class, HttpServletResponse.class, int.class, StepData.class, StepData.class);
        method.setAccessible(true);


        method.invoke(questServlet, request, response, 1, dataStep, dataPrevStep);


        verify(session).invalidate();

    }
    @Test
    void testSessionInvalidatedOnVictory() throws ServletException, IOException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        // Мокаем необходимые объекты
        when(request.getSession()).thenReturn(session);
        when(request.getSession().getAttribute("step")).thenReturn(1);
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        when(request.getParameter("answer")).thenReturn("2");
        when(dataPrevStep.getRightChoice()).thenReturn(2);

        // Через рефлексию ищем приватный метод
        Method method = QuestServlet.class.getDeclaredMethod("processStep", HttpServletRequest.class, HttpServletResponse.class, int.class, StepData.class, StepData.class);
        method.setAccessible(true);


        method.invoke(questServlet, request, response, 1, dataStep, dataPrevStep);


        verify(session).invalidate();

    }


}
