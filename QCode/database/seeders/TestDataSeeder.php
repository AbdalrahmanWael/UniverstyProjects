<?php

namespace Database\Seeders;

use App\Models\User;
use App\Models\Question;
use App\Models\Answer;
use Illuminate\Database\Seeder;
use Illuminate\Support\Facades\Hash;

class TestDataSeeder extends Seeder
{
    public function run()
    {
        // Create test user
        $user = User::create([
            'name' => 'Test User',
            'email' => 'test@example.com',
            'password' => Hash::make('password123'),
        ]);

        // Create a test question
        $question = Question::create([
            'title' => 'How to implement authentication in Laravel?',
            'body' => 'I am new to Laravel and want to implement user authentication. What is the best way to do this?',
            'user_id' => $user->id,
            'code_snippet' => "php artisan make:auth\n// or\nphp artisan ui:auth",
        ]);

        // Attach some tags
        $tags = \App\Models\Tag::whereIn('name', ['php', 'laravel'])->get();
        $question->tags()->attach($tags);

        // Create a test answer
        Answer::create([
            'body' => 'Laravel provides built-in authentication scaffolding. You can use Laravel Breeze or Laravel UI.',
            'code_snippet' => "composer require laravel/breeze --dev\nphp artisan breeze:install",
            'user_id' => $user->id,
            'question_id' => $question->id,
        ]);
    }
}