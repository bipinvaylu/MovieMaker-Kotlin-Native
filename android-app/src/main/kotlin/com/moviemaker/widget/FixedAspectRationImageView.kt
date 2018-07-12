package com.moviemaker.widget

import android.content.Context
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import com.moviemaker.R

class FixedAspectRatioImageView : AppCompatImageView {

    private var aspectRationWidth = DEFAULT_RATIO_WIDTH
    private var aspectRationHeight = DEFAULT_RATIO_HEIGHT

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Constructors

    @JvmOverloads
    @Suppress("ConvertSecondaryConstructorToPrimary")
    constructor(
            context: Context,
            attrs: AttributeSet? = null,
            defStyleAttr: Int = 0
    ) : super(context, attrs, defStyleAttr) {
        val attribute = context.obtainStyledAttributes(attrs, R.styleable.FixedAspectRatioImageView)
        aspectRationWidth = attribute.getInt(
                R.styleable.FixedAspectRatioImageView_aspectRatioWidth,
                DEFAULT_RATIO_WIDTH
        )
        aspectRationHeight = attribute.getInt(
                R.styleable.FixedAspectRatioImageView_aspectRatioHeight,
                DEFAULT_RATIO_HEIGHT
        )
        attribute.recycle()
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Overrides

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val measuredHeight = measuredWidth * aspectRationHeight / aspectRationWidth
        setMeasuredDimension(measuredWidth, measuredHeight)
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Companion

    companion object {
        const val DEFAULT_RATIO_WIDTH = 1
        const val DEFAULT_RATIO_HEIGHT = 1
    }

}