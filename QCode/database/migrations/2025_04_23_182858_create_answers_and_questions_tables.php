<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    public function up()
    {
        // Create answers table first
        Schema::create('answers', function (Blueprint $table) {
            $table->id();
            $table->text('body');
            $table->text('code_snippet')->nullable();
            $table->foreignId('user_id')->constrained()->onDelete('cascade');
            $table->timestamps();
        });

        // Then create questions table with solution_id referencing answers
        Schema::create('questions', function (Blueprint $table) {
            $table->id();
            $table->foreignId('user_id')->constrained()->onDelete('cascade');
            $table->string('title');
            $table->text('body');
            $table->text('code_snippet')->nullable();
            $table->foreignId('solution_id')->nullable()->references('id')->on('answers')->onDelete('set null');
            $table->integer('view_count')->default(0);
            $table->timestamps();
        });

        // Finally add question_id to answers
        Schema::table('answers', function (Blueprint $table) {
            $table->foreignId('question_id')->after('user_id')->constrained()->onDelete('cascade');
        });
    }

    public function down()
    {
        Schema::table('answers', function (Blueprint $table) {
            $table->dropForeign(['question_id']);
        });
        Schema::dropIfExists('questions');
        Schema::dropIfExists('answers');
    }
};