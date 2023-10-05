package com.kkyoungs.diet


import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.w3c.dom.Text
import kotlin.math.pow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val viewModel = viewModel<BmiViewModel>()
            val navController = rememberNavController()
            val bmi = viewModel.bmi.value
            NavHost(navController = navController, startDestination = "home"){
                composable(route = "home"){
                    HomeScreen(){
                            height, weight -> viewModel.bmiCalculate(height , weight)
                    }
                }
                composable(route = "result"){
                    ResultScreen(naviController =navController, bmi = 35.0)
                }
            }
//            ResultScreen(bmi = 35.0)
//            HomeScreen()
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    onResultClicked:(Double, Double) -> Unit,
){
    val (height,setHeight) = remember {
        mutableStateOf("")
    }
    val (weight,setWeight) = remember {
        mutableStateOf("")
    }
    Scaffold (
        topBar = {
            TopAppBar(
                title = {Text("비만도 계산기")}
            )
        },
        content = {
            Column(modifier = Modifier.padding(16.dp)) {

                OutlinedTextField(
                    value = height,
                    onValueChange = setHeight,
                    label = { Text("키") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                )
                OutlinedTextField(
                    value = weight,
                    onValueChange = setWeight,
                    label = { Text("몸무게") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = {
                    if (height.isNotEmpty() && weight.isNotEmpty()){
                        onResultClicked(height.toDouble(), weight.toDouble())
                    }
                },
                    modifier = Modifier.align(Alignment.End)) {
                    Text("결과")

                }
            }

        })
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ResultScreen(naviController: NavController, bmi : Double,){
    val text = when{
        bmi >= 35 -> "고도 비만"
        bmi >= 30 -> "2단계 비만"
        bmi >= 25 -> "1단계 비만"
        bmi >= 23 -> "과체중"
        bmi >= 18.5 -> "정상"
        else -> "저체중"
    }
    val imageRes = when {
        bmi >= 35 -> "고도 비만"
        bmi >= 30 -> "2단계 비만"
        bmi >= 25 -> "1단계 비만"
        bmi >= 23 -> "과체중"
        bmi >= 18.5 -> "정상"
        else -> "저체중"
    }
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("비만도 계산기") },
                navigationIcon = {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "home", modifier = Modifier.clickable {
                        naviController.popBackStack()
                    })
                }
            )
        }
    ) {
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally,) {
            Text(
                "과체중",
                // compose에서 글자크기 는 무조건 sp
                fontSize=30.sp
            )
            Spacer(modifier = Modifier.height(50.dp))
            Image(painter = painterResource(id = R.drawable.baseline_sentiment_dissatisfied_24), contentDescription = null, modifier = Modifier.size(100.dp), colorFilter = ColorFilter.tint(color= Color.Black))

        }
    }
}

class BmiViewModel: ViewModel(){
    private val _bmi = mutableStateOf(0.0)
    val bmi: State<Double> = _bmi

    fun bmiCalculate(
        height : Double, weight: Double,
    ){
        _bmi.value=weight/(height/100.0).pow(2.0)
    }
}

