package com.aegamesi.java_visualizer.ui;

import com.aegamesi.java_visualizer.utils.Utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.TreeSet;

public class ClassCreation implements Serializable {
    public static final String EMPTY = "EMPTY";
    private static final long serialVersionUID = 1L;
    private final String filename;
    private final String packageName;
    private final String className;
    private final boolean insideMain;
    private final boolean writeToFile;
    private final TreeSet<String> imports;
    private final LinkedList<String> attributes;
    private StringBuilder generatedCode;

    public ClassCreation(String filename, boolean insideMain, boolean writeToFile) {
        this.filename = filename;
        this.insideMain = insideMain;
        this.writeToFile = writeToFile;

        String[] parts = filename.split("src/");
        String relativeFilename = parts.length > 1 ? parts[1] : parts[0];

        int lastSlash = relativeFilename.lastIndexOf("/");
        packageName = lastSlash != -1 ? relativeFilename.substring(0, lastSlash).replace("/", ".") : EMPTY;

        String[] partsClassName = filename.split("/");
        className = partsClassName[parts.length - 1];

        imports = new TreeSet<>();
        attributes = new LinkedList<>();
        generatedCode = new StringBuilder();
        addImport("java.io.Serializable");
        addImport("java.util.Objects");

    }

    private static String getFieldName(String attribute) {
        String[] parts = attribute.split(" ");
        return parts[parts.length - 1];
    }

    private String getConstructorEqualsHashcodeAndToString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n\tpublic ").append(className).append("(");
        for (String attribute : attributes) {
            sb.append(attribute).append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(") {\n");
        for (String attribute : attributes) {
            String fieldName = getFieldName(attribute);
            sb.append("\t\tthis.").append(fieldName).append(" = ").append(fieldName).append(";\n");
        }
        sb.append("\t}\n\n");

        sb.append("\t@Override\n");
        sb.append("\tpublic boolean equals(Object o) {\n");
        sb.append("\t\tif (this == o) return true;\n");
        sb.append("\t\tif (o == null || getClass() != o.getClass()) return false;\n");
        sb.append("\t\t").append(className).append(" obj = (").append(className).append(") o;\n");
        sb.append("\t\treturn ");
        for (String attribute : attributes) {
            String fieldName = getFieldName(attribute);
            sb.append("Objects.equals(").append(fieldName).append(", obj.").append(fieldName).append(") && ");
        }
        int length = sb.length();
        sb.replace(length - 4, length, "");
        sb.append(";\n");
        sb.append("\t}\n");

        sb.append("\n\t@Override\n");
        sb.append("\tpublic int hashCode () {\n");
        sb.append("\t\treturn Objects.hash(");
        for (String attribute : attributes) {
            String fieldName = getFieldName(attribute);
            sb.append(fieldName).append(", ");
        }
        length = sb.length();
        sb.replace(length - 2, length, "");
        sb.append(");\n");
        sb.append("\t}\n");

        sb.append("\n\t@Override\n");
        sb.append("\tpublic String toString() {\n");
        sb.append("\t\treturn \" (\" + ");
        for (String attribute : attributes) {
            String fieldName = getFieldName(attribute);
            sb.append(fieldName).append(" + \", \" + ");
        }
        length = sb.length();
        sb.replace(length - 7, length, "");
        sb.append("\")\";\n");
        sb.append("\t}\n");

        return sb.toString();
    }

    public String getCompleteClassCode() {
        StringBuilder sb = new StringBuilder();
        if (!packageName.equals(EMPTY)) {
            sb.append("package ").append(packageName).append(";\n\n");
        }
        for (String anImport : imports) {
            sb.append("import ").append(anImport).append(";\n");
        }
        StringBuilder completeClassCode = new StringBuilder();
        completeClassCode.append(sb).append("\n");
        completeClassCode.append("public class " + className + " implements Serializable {\n");
        completeClassCode.append("\tprivate static final long serialVersionUID = 1L;\n\n");
        for (String attribute : attributes) {
            completeClassCode.append("\tprivate ").append(attribute).append(";\n");
        }
        if (insideMain) {
            completeClassCode.append("\tpublic static void main(String[] args) {\n");
        }
        completeClassCode.append(generatedCode);
        if (insideMain) {
            completeClassCode.append("\t}\n");
        }

        if (!insideMain) {
            completeClassCode.append(getConstructorEqualsHashcodeAndToString());
        }
        completeClassCode.append("}");
        return completeClassCode.toString();
    }

    public void clearCode() {
        imports.clear();
        addImport("java.io.Serializable");
        addImport("java.util.Objects");
        generatedCode = new StringBuilder();
        writeGeneratedClass();
    }

    //    public TreeSet<String> getImports() {
//        return imports;
//    }
//
//    public StringBuilder getGeneratedCode() {
//        return generatedCode;
//    }
//
    public void addImport(String anImport) {
        if (anImport.startsWith("java.lang")) {
            return;
        }
        String importPackage = Utils.getValueTypeNameWithoutParametrizedTypes(anImport).replace("<>", "");
//        String importPackage2 = importPackage;
        if (importPackage.replace(packageName + ".", "").contains(".")) {
            imports.add(importPackage);
        }
//        imports.add(anImport.split("<")[0].split("\\[")[0]);
    }

    public void addAttribute(String typename, String name) {
        attributes.add(Utils.getClassSimpleName(typename) + " " + name);
        addImport(typename);
    }

    public String addCode(String code) {
        code = code.replaceAll("\n", "\n\t\t");
        code = code.substring(0, code.length() - 2);

        generatedCode.append((insideMain ? "\t" : "") + "\t" + code);

        String completeClassCode = getCompleteClassCode();

        writeGeneratedClass();
        return completeClassCode;
    }

    public void writeGeneratedClass() {
        if (!writeToFile) {
            return;
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename + ".java"))) {
            bw.write(getCompleteClassCode());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
