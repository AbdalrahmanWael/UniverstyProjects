<?php

namespace App\Http\Controllers;

use App\Models\Tag;
use Illuminate\Http\Request;

class TagController extends Controller
{
    public function __construct()
    {
        $this->middleware('auth');
    }

    public function store(Request $request)
    {
        $validated = $request->validate([
            'name' => 'required|string|max:30|unique:tags,name',
            'description' => 'nullable|string|max:255'
        ]);

        $tag = Tag::create($validated);

        return response()->json([
            'success' => true,
            'tag' => $tag
        ]);
    }
}