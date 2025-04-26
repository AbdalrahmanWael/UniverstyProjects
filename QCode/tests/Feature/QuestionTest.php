<?php

namespace Tests\Feature;

use Tests\TestCase;
use App\Models\User;
use App\Models\Question;
use App\Models\Tag;
use Illuminate\Foundation\Testing\RefreshDatabase;

class QuestionTest extends TestCase
{
    use RefreshDatabase;

    private $user;
    private $question;

    protected function setUp(): void
    {
        parent::setUp();
        
        // Create test user
        $this->user = User::factory()->create();
        
        // Create question
        $this->question = Question::create([
            'title' => 'Test Question',
            'body' => 'This is a test question body',
            'user_id' => $this->user->id,
            'code_snippet' => 'echo "test";'
        ]);
    }

    public function test_can_create_question_with_tags()
    {
        $tag = Tag::create(['name' => 'test-tag']);
        
        $this->question->tags()->attach($tag);
        
        $this->assertEquals(1, $this->question->tags()->count());
        $this->assertEquals('test-tag', $this->question->tags->first()->name);
    }

    public function test_can_vote_on_question()
    {
        $this->question->votes()->create([
            'user_id' => $this->user->id,
            'value' => 1
        ]);

        $this->assertEquals(1, $this->question->votes()->sum('value'));
    }

    public function test_can_update_question()
    {
        $this->question->update([
            'title' => 'Updated Title',
            'body' => 'Updated body'
        ]);

        $this->assertEquals('Updated Title', $this->question->fresh()->title);
    }
}