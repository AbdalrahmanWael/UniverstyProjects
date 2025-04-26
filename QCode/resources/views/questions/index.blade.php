@extends('layouts.app')

@section('title', 'Questions')

@section('additional-styles')
.question-summary {
    padding: 1.5em;
    border: 1px solid #e1e4e8;
    border-radius: 6px;
    margin-bottom: 1em;
    transition: box-shadow 0.2s;
}
.question-summary:hover {
    box-shadow: 0 0 10px rgba(0,0,0,0.1);
}
.stats {
    display: flex;
    gap: 1.5em;
    color: #586069;
    margin-bottom: 0.8em;
}
.tag {
    display: inline-block;
    padding: 0.4em 0.8em;
    background: #e1ecf4;
    color: #39739d;
    border-radius: 3px;
    margin: 0.3em;
    text-decoration: none;
    transition: background-color 0.2s;
}
.tag:hover {
    background: #cde0ef;
    color: #2c5777;
    text-decoration: none;
}
.question-title {
    font-size: 1.3em;
    color: #0366d6;
    text-decoration: none;
    margin-bottom: 0.5em;
    display: block;
}
.question-title:hover {
    color: #0246a2;
}
.solution-badge {
    color: #2f6f44;
    background: #e1f3e6;
    padding: 0.4em 0.8em;
    border-radius: 3px;
    font-size: 0.9em;
}
.search-section {
    background: #f6f8fa;
    padding: 2em;
    border-radius: 6px;
    margin-bottom: 2em;
}
.sidebar {
    background: #fff;
    padding: 1.5em;
    border: 1px solid #e1e4e8;
    border-radius: 6px;
}
.user-info {
    display: flex;
    align-items: center;
    gap: 0.5em;
    color: #586069;
    margin-top: 1em;
}
.user-avatar {
    width: 32px;
    height: 32px;
    border-radius: 50%;
    background: #e1e4e8;
    display: flex;
    align-items: center;
    justify-content: center;
    color: #586069;
}
@endsection

@section('content')
<div class="container">
    <div class="row">
        <div class="col-md-9">
            <div class="search-section">
                <h1 class="mb-4">All Questions</h1>
                <form action="{{ route('questions.index') }}" method="GET" class="mb-4">
                    <div class="input-group">
                        <input type="text" name="search" class="form-control" placeholder="Search questions..." value="{{ request('search') }}">
                        <button class="btn btn-primary" type="submit">Search</button>
                    </div>
                </form>

                @auth
                    <a href="{{ route('questions.create') }}" class="btn btn-primary">Ask Question</a>
                @else
                    <a href="{{ route('login') }}" class="btn btn-outline-primary">Log in to Ask Question</a>
                @endauth
            </div>

            @if(session('success'))
                <div class="alert alert-success">
                    {{ session('success') }}
                </div>
            @endif

            <div class="questions-list">
                @forelse($questions as $question)
                    <div class="question-summary">
                        <div class="stats">
                            <div class="votes">
                                <strong>{{ $question->votes_count }}</strong> votes
                            </div>
                            <div class="answers {{ $question->solution_id ? 'has-solution' : '' }}">
                                <strong>{{ $question->answers_count }}</strong> answers
                                @if($question->solution_id)
                                    <span class="solution-badge">Solved</span>
                                @endif
                            </div>
                        </div>

                        <a href="{{ route('questions.show', $question) }}" class="question-title">
                            {{ $question->title }}
                        </a>

                        <div class="question-excerpt text-muted">
                            {{ Str::limit(strip_tags($question->body), 200) }}
                        </div>

                        <div class="d-flex justify-content-between align-items-center flex-wrap mt-3">
                            <div class="tags">
                                @foreach($question->tags as $tag)
                                    <a href="{{ route('questions.index', ['tag' => $tag->name]) }}" class="tag">
                                        {{ $tag->name }}
                                    </a>
                                @endforeach
                            </div>

                            <div class="user-info">
                                <div class="user-avatar">
                                    {{ strtoupper(substr($question->user->name, 0, 1)) }}
                                </div>
                                <div>
                                    <a href="{{ route('profile.show', $question->user) }}" class="text-decoration-none">
                                        {{ $question->user->name }}
                                    </a>
                                    <div class="text-muted small">
                                        asked {{ $question->created_at->diffForHumans() }}
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                @empty
                    <div class="alert alert-info">
                        No questions found. @auth Be the first to <a href="{{ route('questions.create') }}">ask a question</a>! @endauth
                    </div>
                @endforelse
            </div>

            <div class="mt-4">
                {{ $questions->links() }}
            </div>
        </div>

        <div class="col-md-3">
            <div class="sidebar">
                <h4>Popular Tags</h4>
                @foreach($tags as $tag)
                    <a href="{{ route('questions.index', ['tag' => $tag->name]) }}" class="tag d-inline-block mb-2">
                        {{ $tag->name }}
                        <span class="text-muted">({{ $tag->questions_count }})</span>
                    </a>
                @endforeach
            </div>
        </div>
    </div>
</div>
@endsection