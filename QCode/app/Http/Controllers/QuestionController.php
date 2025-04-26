<?php

namespace App\Http\Controllers;

use App\Models\Question;
use App\Models\Tag;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\DB;

class QuestionController extends Controller
{
    public function __construct()
    {
        $this->middleware('auth')->except(['index', 'show']);
    }

    public function index(Request $request)
    {
        $query = Question::with(['user', 'tags', 'answers'])
            ->withCount(['votes', 'answers']);

        // Search functionality
        if ($request->has('search')) {
            $searchTerm = $request->get('search');
            $query->where(function($q) use ($searchTerm) {
                $q->where('title', 'like', "%{$searchTerm}%")
                  ->orWhere('body', 'like', "%{$searchTerm}%");
            });
        }

        // Tag filtering
        if ($request->has('tag')) {
            $tagName = $request->get('tag');
            $query->whereHas('tags', function($q) use ($tagName) {
                $q->where('name', $tagName);
            });
        }

        // Sorting
        $sort = $request->get('sort', 'latest');
        switch ($sort) {
            case 'votes':
                $query->orderByVotesCount();
                break;
            case 'answers':
                $query->orderBy('answers_count', 'desc');
                break;
            case 'latest':
            default:
                $query->latest();
                break;
        }

        $questions = $query->paginate(10)->withQueryString();
        $tags = Tag::withCount('questions')
            ->orderBy('questions_count', 'desc')
            ->limit(20)
            ->get();

        return view('questions.index', compact('questions', 'tags'));
    }

    public function create()
    {
        $tags = Tag::orderBy('name')->get();
        return view('questions.create', compact('tags'));
    }

    public function store(Request $request)
    {
        $validated = $request->validate([
            'title' => 'required|max:255',
            'body' => 'required',
            'code_snippet' => 'nullable',
            'tags' => 'required|array|min:1',
            'tags.*' => 'exists:tags,id'
        ]);

        try {
            DB::beginTransaction();

            $question = Question::create([
                'title' => $validated['title'],
                'body' => $validated['body'],
                'code_snippet' => $validated['code_snippet'],
                'user_id' => auth()->id(),
            ]);

            $question->tags()->attach($validated['tags']);

            DB::commit();

            return redirect()->route('questions.show', $question)
                ->with('success', 'Question posted successfully!');

        } catch (\Exception $e) {
            DB::rollBack();
            return back()
                ->withInput()
                ->withErrors(['error' => 'Failed to create question. Please try again.']);
        }
    }

    public function show(Question $question)
    {
        $question->load(['user', 'tags', 'answers.user']);
        $question->incrementViewCount();

        return view('questions.show', compact('question'));
    }

    public function edit(Question $question)
    {
        $this->authorize('update', $question);
        $tags = Tag::orderBy('name')->get();
        return view('questions.edit', compact('question', 'tags'));
    }

    public function update(Request $request, Question $question)
    {
        $this->authorize('update', $question);

        $validated = $request->validate([
            'title' => 'required|max:255',
            'body' => 'required',
            'code_snippet' => 'nullable',
            'tags' => 'sometimes|array|min:1',
            'tags.*' => 'exists:tags,id'
        ]);

        try {
            DB::beginTransaction();

            $question->update([
                'title' => $validated['title'],
                'body' => $validated['body'],
                'code_snippet' => $validated['code_snippet'],
            ]);

            if (isset($validated['tags'])) {
                $question->tags()->sync($validated['tags']);
            }

            DB::commit();

            return redirect()->route('questions.show', $question)
                ->with('success', 'Question updated successfully!');

        } catch (\Exception $e) {
            DB::rollBack();
            return back()
                ->withInput()
                ->withErrors(['error' => 'Failed to update question. Please try again.']);
        }
    }

    public function destroy(Question $question)
    {
        $this->authorize('delete', $question);
        
        $question->delete();
        return redirect()
            ->route('questions.index')
            ->with('success', 'Question deleted successfully!');
    }

    public function vote(Request $request, Question $question)
    {
        $this->authorize('vote', $question);

        $validated = $request->validate([
            'vote' => 'required|in:up,down',
        ]);

        $voteValue = $validated['vote'] === 'up' ? 1 : -1;
        
        $existingVote = $question->votes()
            ->where('user_id', Auth::id())
            ->first();
        
        if ($existingVote) {
            if ($existingVote->value === $voteValue) {
                $existingVote->delete();
                $message = 'Vote removed';
            } else {
                $existingVote->update(['value' => $voteValue]);
                $message = 'Vote updated';
            }
        } else {
            $question->votes()->create([
                'user_id' => Auth::id(),
                'value' => $voteValue
            ]);
            $message = 'Vote added';
        }

        return response()->json([
            'message' => $message,
            'total_votes' => $question->votes()->sum('value'),
            'userVote' => $question->votes()->where('user_id', Auth::id())->first()?->value
        ]);
    }
}