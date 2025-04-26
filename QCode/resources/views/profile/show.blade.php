@extends('layouts.app')

@section('title', $user->name . "'s Profile")

@section('additional-styles')
.profile-header {
    background: white;
    padding: 2rem;
    margin-bottom: 2rem;
    border-radius: 8px;
    box-shadow: 0 1px 3px rgba(0,0,0,0.1);
}
.stats-container {
    display: flex;
    gap: 2rem;
    margin-top: 1rem;
}
.stat-box {
    background: #f8f9fa;
    padding: 1rem;
    border-radius: 4px;
    text-align: center;
}
.stat-value {
    font-size: 1.5rem;
    font-weight: bold;
    color: #0077cc;
}
.stat-label {
    color: #666;
    font-size: 0.9rem;
}
.content-tabs {
    margin: 2rem 0;
}
.tab-content {
    background: white;
    padding: 1rem;
    border-radius: 8px;
    box-shadow: 0 1px 3px rgba(0,0,0,0.1);
}
@endsection

@section('content')
<div class="profile-header">
    <div class="d-flex justify-content-between align-items-start">
        <div>
            <h1>{{ $user->name }}</h1>
            <p class="text-muted">Member since {{ $user->created_at->format('F Y') }}</p>
        </div>
        @can('update', $user)
            <a href="{{ route('profile.edit', $user) }}" class="btn btn-primary">Edit Profile</a>
        @endcan
    </div>

    <div class="stats-container">
        <div class="stat-box">
            <div class="stat-value">{{ $user->questions->count() }}</div>
            <div class="stat-label">Questions</div>
        </div>
        <div class="stat-box">
            <div class="stat-value">{{ $user->answers->count() }}</div>
            <div class="stat-label">Answers</div>
        </div>
        <div class="stat-box">
            <div class="stat-value">
                {{ $user->questions->sum(function($q) { return $q->votes->sum('value'); }) +
                   $user->answers->sum(function($a) { return $a->votes->sum('value'); }) }}
            </div>
            <div class="stat-label">Total Votes</div>
        </div>
    </div>
</div>

<ul class="nav nav-tabs content-tabs" id="profileTabs" role="tablist">
    <li class="nav-item">
        <a class="nav-link active" id="questions-tab" data-bs-toggle="tab" href="#questions" role="tab">
            Questions
        </a>
    </li>
    <li class="nav-item">
        <a class="nav-link" id="answers-tab" data-bs-toggle="tab" href="#answers" role="tab">
            Answers
        </a>
    </li>
</ul>

<div class="tab-content">
    <div class="tab-pane fade show active" id="questions" role="tabpanel">
        @foreach($questions as $question)
            <div class="question-summary mb-3">
                <h3><a href="{{ route('questions.show', $question) }}">{{ $question->title }}</a></h3>
                <div class="d-flex justify-content-between align-items-center text-muted">
                    <div>
                        {{ $question->answers_count }} answers
                        • {{ $question->votes_count }} votes
                        • {{ $question->view_count }} views
                    </div>
                    <div>Asked {{ $question->created_at->diffForHumans() }}</div>
                </div>
            </div>
        @endforeach
        {{ $questions->links() }}
    </div>

    <div class="tab-pane fade" id="answers" role="tabpanel">
        @foreach($answers as $answer)
            <div class="answer-summary mb-3">
                <div class="mb-2">
                    <a href="{{ route('questions.show', $answer->question) }}">{{ $answer->question->title }}</a>
                </div>
                <div class="answer-preview">
                    {{ Str::limit($answer->body, 200) }}
                </div>
                <div class="d-flex justify-content-between align-items-center text-muted mt-2">
                    <div>{{ $answer->votes_count }} votes</div>
                    <div>Answered {{ $answer->created_at->diffForHumans() }}</div>
                </div>
            </div>
        @endforeach
        {{ $answers->links() }}
    </div>
</div>
@endsection