package ru.saveldu.java_junior_quest_game;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StepDataTest {

    @Test
    void testStepDataInitialization() {

        StepData stepData = new StepData("testTitle", "testDescription", 3, "var1", "var2", "", "wrong1", "wrong2", "wrong3");

        //проверки всех полей и правильной инициализации

        assertEquals("testTitle", stepData.getTitle());
        assertEquals("testDescription", stepData.getDescription());
        assertEquals("var1", stepData.getVar1());
        assertEquals("var2", stepData.getVar2());
        assertEquals("", stepData.getVar3());
        assertEquals(3, stepData.getRightChoice());
        assertEquals("wrong1", stepData.getWrong1());
        assertEquals("wrong2", stepData.getWrong2());
        assertEquals("wrong3", stepData.getWrong3());
    }
}
