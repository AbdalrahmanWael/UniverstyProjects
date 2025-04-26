<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\Answer;
use App\Models\Question;
use Illuminate\Support\Facades\Auth;

class AnswerController extends Controller
{
    public function __construct()
    {
        $this->middleware('auth');
    }

    public function store(Request $request, Question $question)
    {
        $validated = $request->validate([
            'body' => 'required|string',
            'code_snippet' => 'nullable|string',
        ]);

        $answer = $question->answers()->create([
            'body' => $validated['body'],
            'code_snippet' => $validated['code_snippet'] ?? null,
            'user_id' => Auth::id(),
        ]);

        return redirect()->route('questions.show', $question)
            ->with('success', 'Answer posted successfully!');
    }

    public function edit(Answer $answer)
    {
        $this->authorize('update', $answer);
        return view('answers.edit', compact('answer'));
    }

    public function update(Request $request, Answer $answer)
    {
        $this->authorize('update', $answer);

        $validated = $request->validate([
            'body' => 'required|string',
            'code_snippet' => 'nullable|string',
        ]);

        $answer->update([
            'body' => $validated['body'],
            'code_snippet' => $validated['code_snippet'] ?? null,
        ]);

        return redirect()->route('questions.show', $answer->question)
            ->with('success', 'Answer updated successfully!');
    }

    public function destroy(Answer $answer)
    {
        $this->authorize('delete', $answer);
        $question = $answer->question;
        
        $answer->delete();
        return redirect()->route('questions.show', $question)
            ->with('success', 'Answer deleted successfully!');
    }

    public function vote(Request $request, Answer $answer)
    {
        $this->authorize('vote', $answer);

        $validated = $request->validate([
            'vote' => 'required|in:up,down',
        ]);

        $voteValue = $validated['vote'] === 'up' ? 1 : -1;
        
        $existingVote = $answer->votes()->where('user_id', Auth::id())->first();
        
        if ($existingVote) {
            if ($existingVote->value === $voteValue) {
                $existingVote->delete();
                $message = 'Vote removed';
            } else {
                $existingVote->update(['value' => $voteValue]);
                $message = 'Vote updated';
            }
        } else {
            $answer->votes()->create([
                'user_id' => Auth::id(),
                'value' => $voteValue
            ]);
            $message = 'Vote added';
        }

        return response()->json([
            'message' => $message,
            'total_votes' => $answer->votes()->sum('value'),
            'userVote' => $answer->votes()->where('user_id', Auth::id())->first()?->value
        ]);
    }

    public function markAsSolution(Answer $answer)
    {
        $question = $answer->question;
        $this->authorize('markSolution', [$answer, $question]);

        $question->update(['solution_id' => $answer->id]);

        return redirect()->route('questions.show', $question)
            ->with('success', 'Answer marked as solution!');
    }
}