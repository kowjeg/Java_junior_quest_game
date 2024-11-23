package ru.saveldu.java_junior_quest_game;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.HashMap;

@WebServlet("/quest")
public class QuestServlet extends HttpServlet {


    private static HashMap<Integer, StepData> questStepMap = StepData.getMap();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession();

        Integer currStep = (Integer) session.getAttribute("step");
        if (currStep == null) {
            currStep = 0;
            session.setAttribute("step", currStep);
        }

        resp.setContentType("text/html; charset=UTF-8");
        req.setCharacterEncoding("UTF-8");


        StepData stepData = null;
        if (questStepMap.containsKey(currStep)) {
            stepData = questStepMap.get(currStep);
        }

        StepData prevStep = null;
        if (currStep > 0 && questStepMap.containsKey(currStep - 1)) {
            prevStep = questStepMap.get(currStep - 1);
        }

        processStep(req, resp, currStep, stepData, prevStep);
    }

    private String checkAnswerForNull(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String answer = req.getParameter("answer");
        if (answer == null) {
            renderGameOver(req, resp, "Что-то пошло не так");
            return null; // Завершаем выполнение, не продолжая выполнение метода
        }
        return answer;
    }

    private void processStep(HttpServletRequest req, HttpServletResponse resp, int step, StepData dataStep, StepData dataPrevStep) throws ServletException, IOException {

        switch (step) {
            case 0 -> renderFirstStep(req, resp, dataStep);
            case 1,2,3,4,5,6,7 -> renderNextStep(req, resp, dataStep, dataPrevStep);

            default -> renderGameOver(req, resp, "Шаг не найден.");
        }

    }
    private void renderFirstStep(HttpServletRequest req, HttpServletResponse resp, StepData dataStep) throws ServletException, IOException {

        req.setAttribute("head_text", dataStep.getTitle());
        req.setAttribute("info_text", dataStep.getDescription());
        req.setAttribute("var1", dataStep.getVar1());
        req.setAttribute("var2", dataStep.getVar2());
        req.setAttribute("var3", dataStep.getVar3());
        HttpSession session = req.getSession();
        session.setAttribute("step",1);



        req.getRequestDispatcher("quest.jsp").forward(req, resp);
    }

    private void renderNextStep(HttpServletRequest req, HttpServletResponse resp, StepData dataStep, StepData dataPrevStep) throws ServletException, IOException {
        if((int) req.getSession().getAttribute("step")==7) {
            req.getSession().invalidate();
            req.getRequestDispatcher("victory.jsp").forward(req, resp);
        }
        String answer = checkAnswerForNull(req, resp);
        String rightChoice = String.valueOf(dataPrevStep.getRightChoice());
        if (answer.equals(rightChoice)) {
            req.setAttribute("head_text", dataStep.getTitle());
            req.setAttribute("info_text", dataStep.getDescription());
            req.setAttribute("var1", dataStep.getVar1());
            req.setAttribute("var2", dataStep.getVar2());
            req.setAttribute("var3", dataStep.getVar3());
            HttpSession session = req.getSession();
            int currentStep = (int) session.getAttribute("step"); // Получаем текущий шаг из сессии
//            req.setAttribute("step", currentStep);
            session.setAttribute("step", ++currentStep); // Сохраняем обновленный шаг в сессии

            req.getRequestDispatcher("quest.jsp").forward(req, resp);
        }
        switch (answer) {
            case "1" -> renderGameOver(req, resp, dataPrevStep.getWrong1());
            case "2" -> renderGameOver(req, resp, dataPrevStep.getWrong2());
            case "3" -> renderGameOver(req, resp, dataPrevStep.getWrong3());

            default -> renderGameOver(req, resp, "Некорректный ответ.");

        }
    }

    private void renderGameOver(HttpServletRequest req, HttpServletResponse resp, String message) throws ServletException, IOException {
        req.setAttribute("text_to_lose", message);
        req.getRequestDispatcher("gameover.jsp").forward(req, resp);
        req.getSession().invalidate();
    }
}
