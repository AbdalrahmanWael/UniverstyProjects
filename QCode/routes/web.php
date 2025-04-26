<?php

use Illuminate\Support\Facades\Route;
use App\Http\Controllers\AuthController;
use App\Http\Controllers\QuestionController;
use App\Http\Controllers\AnswerController;
use App\Http\Controllers\ProfileController;
use App\Http\Controllers\TagController;

/*
|--------------------------------------------------------------------------
| Web Routes
|--------------------------------------------------------------------------
|
| Here is where you can register web routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| contains the "web" middleware group. Now create something great!
|
*/

Route::get('/', [QuestionController::class, 'index'])->name('home');

// Authentication Routes
Route::get('/login', [AuthController::class, 'showLoginForm'])->name('login');
Route::post('/login', [AuthController::class, 'login']);
Route::get('/register', [AuthController::class, 'showRegisterForm'])->name('register');
Route::post('/register', [AuthController::class, 'register']);
Route::post('/logout', [AuthController::class, 'logout'])->name('logout');

// Question Routes
Route::get('/questions', [QuestionController::class, 'index'])->name('questions.index');
Route::get('/questions/create', [QuestionController::class, 'create'])->name('questions.create');
Route::post('/questions', [QuestionController::class, 'store'])->name('questions.store');
Route::get('/questions/{question}', [QuestionController::class, 'show'])->name('questions.show');
Route::get('/questions/{question}/edit', [QuestionController::class, 'edit'])->name('questions.edit');
Route::put('/questions/{question}', [QuestionController::class, 'update'])->name('questions.update');
Route::delete('/questions/{question}', [QuestionController::class, 'destroy'])->name('questions.destroy');
Route::post('/questions/{question}/vote', [QuestionController::class, 'vote'])->name('questions.vote');

// Answer Routes
Route::post('/questions/{question}/answers', [AnswerController::class, 'store'])->name('answers.store');
Route::get('/answers/{answer}/edit', [AnswerController::class, 'edit'])->name('answers.edit');
Route::put('/answers/{answer}', [AnswerController::class, 'update'])->name('answers.update');
Route::delete('/answers/{answer}', [AnswerController::class, 'destroy'])->name('answers.destroy');
Route::post('/answers/{answer}/vote', [AnswerController::class, 'vote'])->name('answers.vote');
Route::post('/answers/{answer}/solution', [AnswerController::class, 'markAsSolution'])->name('answers.solution');

// Profile Routes
Route::get('/profile/{user}', [ProfileController::class, 'show'])->name('profile.show');
Route::get('/profile/{user}/edit', [ProfileController::class, 'edit'])->name('profile.edit');
Route::put('/profile/{user}', [ProfileController::class, 'update'])->name('profile.update');

// Tag Routes
Route::post('/tags', [TagController::class, 'store'])->name('tags.store');
