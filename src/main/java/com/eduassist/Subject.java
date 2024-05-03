package com.eduassist;

public class Subject {

    private final String code;
    private final String name;
    private final String teacher;
    private final String classroom;
    private final float[] percentage;
    private final float[] grades;
    private final String[] gradeNames;
    private final float[][] schedule;            // día, hora, tiempo
    private float totalAverage;
    private float weightedAverage;
    private float targetGrade;
    private float neededGrade;
    private final int credits;
    private final int umes;
    private float targetAverage;
    private String subjectColor;
    private String textColor;

    public Subject(String code, String name, String teacher, String classroom, String percentage, String grades, String gradeNames, String schedule, int credits, int umes, float targetAverage, String subjectColor, String textColor) {

        this.code = code;
        this.name = name;
        this.teacher = teacher;
        this.classroom = classroom;
        this.percentage = transformGrades(percentage);
        this.grades = transformGrades(grades);
        this.gradeNames = gradeNames.split(",");
        this.schedule = transformSchedule(schedule);
        this.credits = credits;
        this.umes = umes;
        this.targetAverage = targetAverage;
        this.subjectColor = subjectColor;
        this.textColor = textColor;

        this.totalAverage = calculateTotalAverage(this.percentage, this.grades);
        this.weightedAverage = calculateWeightedAverage(this.percentage, this.grades);
        this.neededGrade = calculateNeededGrade(3, this.percentage, this.grades);
        this.targetGrade = calculateNeededGrade(this.targetAverage, this.percentage, this.grades);

    }

    // ------------------------ //   TRANSFORMAR DATOS STRING   // ------------------------ //

    private float[] transformGrades(String txt) {
        String[] stringList = txt.split(",");
        float[] grades = new float[stringList.length];

        for (int i = 0; i < stringList.length; i++) {
            grades[i] = Float.parseFloat(stringList[i]);
        }

        return grades;
    }

    private float[][] transformSchedule(String txt) {

        String[] groups = txt.split(",");
        float[][] schedule = new float[groups.length][3];

        for (int i = 0; i < groups.length; i++) {
            String[] group = groups[i].split(" ");
            for (int j = 0; j < group.length; j++) {
                schedule[i][j] = Float.parseFloat(group[j]);
            }
        }

        return schedule;
    }

    // ------------------------ //   CÁLCULOS   // ------------------------ //

    private float calculateTotalAverage(float[] perc, float[] grades) {

        float av = 0;

        for (int i = 0; i < perc.length; i++) {
            if (grades[i] >= 0) {
                av += perc[i] * grades[i];
            }
        }

        av = (float) Math.floor(av * 10) / 10;            // Se redondea hacia abajo

        return av;
    }

    public static float calculateWeightedAverage(float[] perc, float[] grades) {

        float av = 0;
        float sum = 0;

        for (int i = 0; i < perc.length; i++) {
            if (grades[i] >= 0) {
                av += perc[i] * grades[i];
                sum += perc[i];
            }
        }

        av /= sum;
        av = (float) Math.floor(av * 10) / 10;            // Se redondea haca abajo

        return av;

    }

    public static float calculateNeededGrade(float target, float[] perc, float[] grades) {

        float grade, gradesSum, percSum;

        gradesSum = 0;
        percSum = 1;

        for (int i = 0; i < perc.length; i++) {
            if (grades[i] >= 0) {
                gradesSum += perc[i] * grades[i];
                percSum -= perc[i];
            }
        }

        grade = (target - gradesSum) / percSum;
        grade = (float) Math.ceil(grade * 10) / 10;              // Se redondea hacia arriba

        if (grade < 0) {            // Se iguala la nota a 0 por si el estudiante ya tiene su promedio objetivo
            grade = 0;
        }

        return grade;

    }

    // ------------------------ //   SET & UPDATE   // ------------------------ //

    public void setTargetAverage(float targetAverage) {
        this.targetAverage = targetAverage;
        updateInfo();
        DataBase.updateTarget(code, targetAverage);
    }

    public void setColors(String subjectColor, String textColor) {
        this.subjectColor = subjectColor;
        this.textColor = textColor;
        DataBase.updateColor(code, subjectColor, textColor);
    }

    private void updateInfo() {
        this.totalAverage = calculateTotalAverage(this.percentage, this.grades);
        this.weightedAverage = calculateWeightedAverage(this.percentage, this.grades);
        this.neededGrade = calculateNeededGrade(3, this.percentage, this.grades);
        this.targetGrade = calculateNeededGrade(this.targetAverage, this.percentage, this.grades);
    }

    // ------------------------ //   GET   // ------------------------ //

    public String getCode() {
        return this.code;
    }

    public String getName() {
        return this.name;
    }

    public String getTeacher() {
        return this.teacher;
    }

    public String getClassroom() {
        return this.classroom;
    }

    public float[] getPercentage() {
        return this.percentage;
    }

    public float[] getGrades() {
        return this.grades;
    }

    public String[] getGradeNames() {
        return this.gradeNames;
    }

    public float[][] getSchedule() {
        return this.schedule;
    }

    public float getTotalAverage() {
        return this.totalAverage;
    }

    public float getWeightedAverage() {
        return this.weightedAverage;
    }

    public float getNeededGrade() {
        return this.neededGrade;
    }

    public int getCredits() {
        return this.credits;
    }

    public int getUmes() {
        return this.umes;
    }

    public float getTargetGrade() {
        return this.targetGrade;
    }

    public float getTargetAverage() {
        return targetAverage;
    }

    public String getSubjectColor() {
        return this.subjectColor;
    }

    public String getTextColor() {
        return this.textColor;
    }
}
