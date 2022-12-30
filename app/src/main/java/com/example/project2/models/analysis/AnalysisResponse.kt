package com.example.project2.models.analysis

data class AnalysisResponse(
    val Diseases: List<Disease>,
    val VariableImportances: List<VariableImportance>,
    val status: String
)